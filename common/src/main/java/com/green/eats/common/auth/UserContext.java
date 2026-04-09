package com.green.eats.common.auth;

import com.green.eats.common.model.UserDto;

public class UserContext {
    //요청이 올 때 너의 개인공간을 만들어주는 친구
    private static final ThreadLocal<UserDto> USER_HOLDER = new ThreadLocal<>();

    public static void set(UserDto user) { USER_HOLDER.set(user); }
    public static UserDto get() { return USER_HOLDER.get(); }
    public static void clear() { USER_HOLDER.remove(); }
}
