package com.tek.audit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tek.audit.model.json.AuditWebRequest;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"request"})

@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

@Entity
@Table(name = "web_audit", schema = "audit")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class WebAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Type(type = "jsonb")
  @Column(name = "request", columnDefinition = "jsonb")
  @Basic
  private AuditWebRequest request;

  @Column(name = "status")
  private Integer status;

  @Column(name = "duration")
  private Long duration;

  @Column(name = "created_at")
  @CreatedDate
  private Instant createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private Instant updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    WebAudit webAudit = (WebAudit) o;
    return id.equals(webAudit.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
