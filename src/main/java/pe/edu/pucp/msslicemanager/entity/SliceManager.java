package pe.edu.pucp.msslicemanager.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Data
@Entity
@Table(name = "PSTK_SLICE", schema = "pucp_stack")
public class SliceManager {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "SLICE_ID", nullable = false)
    private Integer sliceId;

    @Basic
    @Column(name = "NAME", nullable = false, length = 45)
    @NotNull(message = "The name must not be null.")
    @NotBlank(message = "The name must not be empty.")
    @Size(max = 45, message = "The name must not be more than 45 characters.")
    private String sliceName;

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 255)
    @Size(max = 255, message = "The description must not be more than 255 characters.")
    private String description;

    @Basic
    @Column(name = "STATUS", nullable = false)
    @NotNull(message = "The status must not be null.")
    private Boolean status;

}
