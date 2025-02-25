package muon.app.ui.components.settings;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditorEntry {
    private String name;
    private String path;

    public EditorEntry() {
        
    }
    public EditorEntry(String name, String path) {
        super();
        this.name = name;
        this.path = path;
    }

}
