package net.dancier.kikeriki.adapter.out.persistence.state;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@Table(name ="state")
@Entity
public class StateJpaEntity {

   @Id
   private String dancer_id;

   @JdbcTypeCode(SqlTypes.JSON)
   @Column(name = "data")
   private Data data;


  @lombok.Data
  public static class Data {

  }

}
