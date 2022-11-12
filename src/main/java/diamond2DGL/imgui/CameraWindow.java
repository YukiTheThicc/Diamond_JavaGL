package diamond2DGL.imgui;

import diamond2DGL.Camera;
import diamond2DGL.Container;
import imgui.ImGui;

public class CameraWindow {

    public void imgui() {
        Camera camera = Container.getCamera();
        ImGui.begin("Camera");

        camera.setZoom(DiaImGui.dragFloat("Camera Zoom", camera.getZoom()));

        ImGui.end();
    }
}
