package diamond2DGL.editor;

import diamond2DGL.observers.EventSystem;
import diamond2DGL.observers.events.Event;
import diamond2DGL.observers.events.EventType;
import imgui.ImGui;

public class MenuBar {

    public void imgui() {
        ImGui.beginMainMenuBar();
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Load", "Ctrl+L")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
    }
}
