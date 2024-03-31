package net.dancier.kikeriki.adapter.out.userinfo;

import net.dancier.kikeriki.application.port.UserInfoDto;
import net.dancier.kikeriki.application.port.UserInfoPort;
import org.springframework.stereotype.Component;

@Component
public class UserInfoAdapter implements UserInfoPort {
  @Override
  public UserInfoDto loadByDancerId(String dancerId) {
    UserInfoDto userInfoDto = new UserInfoDto();
    userInfoDto.setDancerId(dancerId);
    userInfoDto.setEmailAddress("dancer@dancier.net");
    return userInfoDto;
  }
}
