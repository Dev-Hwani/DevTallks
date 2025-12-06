package com.mysite.devtallks.member.constant;

public enum MemberRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    MemberRole(String roleName) {
        this.roleName = roleName;
    }

    public String roleName() {
        return roleName;
    }
}
