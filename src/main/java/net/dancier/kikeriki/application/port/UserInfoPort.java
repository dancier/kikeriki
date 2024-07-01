package net.dancier.kikeriki.application.port;

public interface UserInfoPort {

  UserInfoDto loadByDancerId(String dancerId);

}
