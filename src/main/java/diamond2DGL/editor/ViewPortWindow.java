package diamond2DGL.editor;

import diamond2DGL.Display;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.observers.EventSystem;
import diamond2DGL.observers.events.Event;
import diamond2DGL.observers.events.EventType;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;


public class ViewPortWindow {

    private float leftX, rightX, topY, bottomY;
    private boolean isPlaying = false;

    public void imgui(){
        ImGui.begin("Game ViewPort", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
        | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
            isPlaying = true;
            EventSystem.notify(null, new Event(EventType.Play));
        }
        if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
            isPlaying = false;
            EventSystem.notify(null, new Event(EventType.Stop));
        }
        ImGui.endMenuBar();

        ImVec2 portSize = getMaxPortSize();
        ImVec2 portPos = getCenteredPos(portSize);
        ImGui.setCursorPos(portPos.x, portPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + portSize.x;
        topY = topLeft.y + portSize.y;

        int textureID = Display.getFrameBuffer().getTextureID();
        ImGui.image(textureID, portSize.x, portSize.y, 0,1,1,0);

        MouseListener.setViewPortPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setViewPortSize(new Vector2f(portSize.x, portSize.y));

        ImGui.end();
    }

    public boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

    private ImVec2 getMaxPortSize() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Display.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // Put black bars
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Display.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPos(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float portX = (windowSize.x / 2.0f) - (aspectSize.x) / 2.0f;
        float portY = (windowSize.y / 2.0f) - (aspectSize.y) / 2.0f;

        return  new ImVec2(portX + ImGui.getCursorPosX(), portY + ImGui.getCursorPosY());
    }
}
