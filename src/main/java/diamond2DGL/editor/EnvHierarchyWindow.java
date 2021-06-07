package diamond2DGL.editor;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class EnvHierarchyWindow {

    private static String payloadDragDropType = "EnvHierarchy";

    public void imgui() {
        ImGui.begin("Scene Hierarchy");
        List<Entity> entities = Container.getEnv().getEntities();
        int index = 0;
        for (Entity e : entities) {
            if (!e.isToSerialize()) {
                continue;
            }
            boolean treeNodeOpen = doTreeNode(e, index);
            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }

    private boolean doTreeNode(Entity e, int index) {
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                e.name,
                ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth,
                e.name
        );
        ImGui.popID();

        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayloadObject(payloadDragDropType, e);
            ImGui.text(e.name);

            ImGui.endDragDropSource();
        }

        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayloadObject(payloadDragDropType);
            if (payload != null) {
                if (payload.getClass().isAssignableFrom(Entity.class)) {
                    Entity acceptedPayload = (Entity) payload;
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
