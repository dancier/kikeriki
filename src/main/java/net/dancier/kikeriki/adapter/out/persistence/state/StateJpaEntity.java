package net.dancier.kikeriki.adapter.out.persistence.state;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import net.dancier.kikeriki.application.port.StateDto;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Table(name ="state")
@Entity
public class StateJpaEntity {

   @Id
   private String dancerId;

   @JdbcTypeCode(SqlTypes.JSON)
   @Column(name = "data")
   private StateDto data;

}
