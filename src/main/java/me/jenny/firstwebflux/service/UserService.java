//package me.jenny.firstwebflux.service;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    Utils utils;
//
//    public Mono<UserResponse> registerUser(UserRequest userRequest) {
//        User newUser = new User();
//        BeanUtils.copyProperties(userRequest, newUser);
//        if (userRequest.getPassword() != null) {
//            newUser.setPassword(utils.encryptPassword(userRequest.getPassword()));
//        }
//        newUser.setUserId(utils.generateUserId());
//
//        UserResponse userResponse = new UserResponse();
//
//        return userRepository.findByEmail(userRequest.getEmail())
//                .flatMap(user -> Mono.just(new UserResponse(false, "user exists", null))
//                )
//                .switchIfEmpty(
//                        userRepository.save(newUser.setAsNew())
//                                .map(savedUser -> {
//                                    userResponse.setSuccess(true);
//                                    userResponse.setMessage("success");
//                                    userResponse.setData(new UserDto(savedUser.getUserId(), savedUser.getName(),
//                                            savedUser.getEmail(), savedUser.getPhoneNumber()));
//                                    return userResponse;
//                                })
//                );
//    }
//
//    public Mono<Object> loginUser(UserRequest userRequest) {
//        UserResponse userResponse = new UserResponse();
//
//        return userRepository.findByEmail(userRequest.getEmail())
//                .map(user -> {
//                    if (utils.checkPassword(userRequest.getPassword())) {
//                        userResponse.setSuccess(true);
//                        userResponse.setMessage("success");
//                        userResponse.setData(new UserDto(user.getUserId(),
//                                user.getName(), user.getEmail(), user.getPhoneNumber()));
//                        return userResponse;
//                    }
//                    return Mono.just(new UserResponse(false, "wrong password", null));
//
//                })
//                .switchIfEmpty(Mono.just(new UserResponse(false, "wrong email", null)));
//    }
//
//    public Mono<UserResponse> updateUserDetails(UserRequest userRequest) {
//        return userRepository.updateUserDetails(userRequest.getPhoneNumber(), userRequest.getEmail(), userRequest.getUserId())
//                .flatMap(result -> Mono.just(new UserResponse(true, "success", null)));
//    }
//
//    public Mono<UserResponse> updatePassword(UserRequest userRequest) {
//        String encryptedPassword = utils.encryptPassword(userRequest.getPassword());
//        return userRepository.updatePassword(encryptedPassword, userRequest.getUserId())
//                .flatMap(result -> Mono.just(new UserResponse(true, "success", null)));
//    }
//
//    public Mono<UserResponse> getUserById(UserRequest userRequest) {
//        UserResponse userResponse = new UserResponse();
//        return userRepository.findById(userRequest.getUserId())
//                .map(userDetails ->{
//                    userResponse.setSuccess(true);
//                    userResponse.setMessage("success");
//                    userResponse.setData(new UserDto(userDetails.getUserId(),
//                            userDetails.getName(), userDetails.getEmail(), userDetails.getPhoneNumber()));
//                    return userResponse;
//                })
//                .switchIfEmpty(Mono.just(new UserResponse(false, "fail", null)));
//    }
//
//    public Mono<UserResponse> getUserByEmail(UserRequest userRequest) {
//        UserResponse userResponse = new UserResponse();
//        return userRepository.findByEmail(userRequest.getEmail())
//                .map(userDetails ->{
//                    userResponse.setSuccess(true);
//                    userResponse.setMessage("success");
//                    userResponse.setData(new UserDto(userDetails.getUserId(),
//                            userDetails.getName(), userDetails.getEmail(), userDetails.getPhoneNumber()));
//                    return userResponse;
//                })
//                .switchIfEmpty(Mono.just(new UserResponse(false, "fail", null)));
//    }
//
//    // Todo clean up
//    public Flux<AllUsersResponse> getAllUsers() {
//        AllUsersResponse allUsersResponse = new AllUsersResponse();
//        allUsersResponse.setSuccess(true);
//        allUsersResponse.setMessage("success");
//
//        return userRepository.findAll()
//                .map(users ->{
//                    allUsersResponse.setData(List.of(
//                            new UserDto(users.getUserId(),users.getName(),users.getEmail(),
//                                    users.getPhoneNumber())));
//                    return allUsersResponse;
//                });
//    }
//}
