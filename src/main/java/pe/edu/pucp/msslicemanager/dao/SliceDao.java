package pe.edu.pucp.msslicemanager.dao;

import lombok.Data;
import org.springframework.stereotype.Component;
import pe.edu.pucp.msslicemanager.entity.SliceManager;

@Data
@Component
public class SliceDao {
    private SliceManager slice;
}
