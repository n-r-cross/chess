package service;

import server.RegisterRequest;
import server.RegisterResponse;

public class RegisterService {
    public RegisterResponse register(RegisterRequest r){
        RegisterResponse response = new RegisterResponse("AlfredTheButtler");
        return response;
    }
}
