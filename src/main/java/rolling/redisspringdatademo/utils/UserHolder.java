package rolling.redisspringdatademo.utils;

import rolling.redisspringdatademo.controller.dto.UserDto;

public class UserHolder {
    private static final ThreadLocal<UserDto> tl = new ThreadLocal<>();

    public static void saveUser(UserDto userId) { tl.set(userId); }

    public static UserDto getUser() { return tl.get(); }

    public static void removeUser() { tl.remove(); }
}
