package org.itstep.auth_project.controllers;

import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.itstep.auth_project.config.StaticConfig;
import org.itstep.auth_project.entities.DbUser;
import org.itstep.auth_project.entities.Role;
import org.itstep.auth_project.models.UserModel;
import org.itstep.auth_project.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class WebController {

    @Value("${file.ava.dir}")
    private String avaBaseUrl;

    @Value("${file.defaultava.dir}")
    private String defaultAvaDir;

    @Value("${file.ava.classpath}")
    private String avaClasspath;

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "register")
    public String registerPage(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "register";
    }

    @PostMapping(value = "register")
    public String registerUser(@ModelAttribute UserModel userModel, @RequestParam(name = "ava_pic") MultipartFile file) {
        if (userModel.getPassword().equals(userModel.getConfirmPassword())) {
            List<Role> roles = new ArrayList<>();
            roles.add(StaticConfig.ROLE_USER);
            DbUser dbUser = new DbUser(userModel.getEmail(), userModel.getPassword(), userModel.getFullName(),null, null, roles);
            userService.registerUser(dbUser);
            doLoadPhoto(dbUser, file);
            return "redirect:/login";
        }
        else {
            return "redirect:/register?error=1";
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/profile")
    public String profile() {
        return "profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/upload-ava")
    public String uploadAva(@RequestParam(name = "ava_pic") MultipartFile file) {
        DbUser currentUser = getUser();
        doLoadPhoto(currentUser, file);
        return "redirect:/profile";
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/view-photo/{avaHash}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] viewPhoto(@PathVariable("avaHash") String avaHash) {
        String image = defaultAvaDir + "avatar-profile-picture.jpg";
        if (!avaHash.equals("null")){
            image = avaClasspath + avaHash;
        }

        InputStream in;

        try {
            ClassPathResource resource = new ClassPathResource(image);
            in = resource.getInputStream();
        } catch (IOException e) {
            image = defaultAvaDir + "avatar-profile-picture.jpg";
            ClassPathResource resource = new ClassPathResource(image);
            in = resource.getInputStream();
        }
        return IOUtils.toByteArray(in);
    }

    @ModelAttribute
    public void addUser(Model model) {
        model.addAttribute("user", getUser());
    }

    private DbUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User securityUser = (User) authentication.getPrincipal();
            DbUser user = userService.getUser(securityUser.getUsername());
            return user;
        }
        return null;

    }

    private void doLoadPhoto(DbUser currentUser, MultipartFile file) {
        if(Objects.nonNull(currentUser)) {
            if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                String uniqueName = DigestUtils.sha1Hex("user_avatar_" + currentUser.getId());
                String ext = "png";
                if (file.getContentType().equals("image/jpeg")) {
                    ext = "jpg";
                }
                try {
                    String fullPath = avaBaseUrl + uniqueName + "." + ext;
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(fullPath);
                    Files.write(path, bytes);
                    currentUser.setAva(fullPath);
                    currentUser.setAvaHash(uniqueName + "." + ext);
                    userService.updateUser(currentUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
