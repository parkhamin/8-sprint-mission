package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(name = "username", length = 50, nullable = false, unique = true)
  private String username;

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;

  @Column(name = "password", length = 60, nullable = false)
  private String password;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @Setter(AccessLevel.PROTECTED)
  @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
  private UserStatus status;

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
    }

    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }

    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }

    if (newProfile != null) {
      this.profile = newProfile;
    }
  }
}
