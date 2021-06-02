package diamond2DGL.editor;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;

import java.util.List;

public class EnvHeirarchyWindow {

    public void imgui() {
        ImGui.begin("Scene Heirarchy");
        List<Entity> entities = Container.getEnv().getEntities();
        int index = 0;
        for (Entity e : entities) {
            if (!e.isToSerialize()) {
                continue;
            }
            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(
                    e.name,
                    ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding |
                            ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth,
                    e.name
            );
            ImGui.popID();
            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }
}
