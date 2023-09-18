package com.iteh.todobackend.auth;
import io.swagger.annotations.Api;
import com.iteh.todobackend.entity.User;
import com.iteh.todobackend.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/auth")
@Api(tags = "Authentication")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @ApiOperation(value = "Registracija korisnika", response = AuthenticationResponse.class)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try{
            return ResponseEntity.ok(authService.register(request));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User already exists");
        }
    }

    @ApiOperation(value = "Autentifikacija korisnika", response = AuthenticationResponse.class)
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request, HttpServletRequest servletRequest) {


        try {
            AuthenticationResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @ApiOperation(value = "Odjava korisnika")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        String username = request.getUsername();

        if (authService.isUserLoggedIn(username)) {
            authService.logout(username);
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not logged in.");
        }
    }
    @ApiOperation(value = "Obnovi JWT token")
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
    @ApiOperation(value = "Vrati detalje korisnika", response = UserResponse.class)
    @GetMapping("/getUser")
    public ResponseEntity<UserResponse> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            // User not found in the database
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserResponse userResponse = convertUserToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    // Helper method to convert User entity to UserResponse DTO
    private UserResponse convertUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        // Set other necessary fields

        return userResponse;
    }



}
