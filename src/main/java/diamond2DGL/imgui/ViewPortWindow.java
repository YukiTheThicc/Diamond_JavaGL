package diamond2DGL.imgui;

import diamond2DGL.Window;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.observers.EventSystem;
import diamond2DGL.observers.events.Event;
import diamond2DGL.observers.events.EventType;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class ViewPortWindow {

    // ATTRIBUTES
    private int leftX, rightX, topY, bottomY;
    private int sizeX, sizeY;
    private boolean isPlaying = false;

    // GETTERS & SETTERS
    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    // METHODS
    public void imgui() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
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


        ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY());
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);
        this.sizeX = (int) windowSize.x;
        this.sizeY = (int) windowSize.y;
        leftX = (int) windowPos.x + 8;
        rightX = (int) windowPos.x + sizeX + 8;
        bottomY = (int) windowPos.y + 26;
        topY = (int) windowPos.y + sizeY + 26;

        int textureId = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureId, sizeX, sizeY, 0, 1, 1, 0);
        System.out.println("--ViewPort-- SizeX: " + sizeX + ", SizeY: " + sizeY);

        MouseListener.setGameViewportPos(new Vector2f(windowPos.x + 8, windowPos.y + 26));
        MouseListener.setGameViewportSize(new Vector2f(sizeX, sizeY));

        ImGui.end();
    }

    public boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }
}
