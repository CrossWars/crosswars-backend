package xyz.crossward.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "IS_MEMBER")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class IsMember(
    @Id @Column(name = "user_id") val userId: String,
    @Id @Column(name = "group_id") val groupId: String
): Serializable