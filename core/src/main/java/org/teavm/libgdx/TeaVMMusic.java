package org.teavm.libgdx;

import org.teavm.dom.browser.Window;
import org.teavm.dom.events.Event;
import org.teavm.dom.events.EventListener;
import org.teavm.dom.html.HTMLAudioElement;
import org.teavm.jso.JS;
import com.badlogic.gdx.audio.Music;

/**
 *
 * @author Alexey Andreev
 */
public class TeaVMMusic implements Music {
    private static Window window = (Window)JS.getGlobal();
    private HTMLAudioElement element;
    private boolean started;
    private OnCompletionListener listener;

    public TeaVMMusic(TeaVMFileHandle file) {
        element = (HTMLAudioElement)window.getDocument().createElement("audio");
        element.setSrc("assets/" + file.path());
        element.addEventListener("ended", new EventListener() {
            @Override public void handleEvent(Event evt) {
                if (listener != null) {
                    listener.onCompletion(TeaVMMusic.this);
                }
            }
        });
        window.getDocument().getBody().appendChild(element);
    }

    private void checkDisposed() {
        if (element == null) {
            throw new IllegalStateException("This music instance is already disposed");
        }
    }

    @Override
    public void play() {
        checkDisposed();
        element.play();
        started = true;
    }

    @Override
    public void pause() {
        checkDisposed();
        element.pause();
    }

    @Override
    public void stop() {
        checkDisposed();
        element.pause();
        element.setCurrentTime(0);
        started = false;
    }

    @Override
    public boolean isPlaying() {
        checkDisposed();
        return started && !element.isPaused() && element.isEnded();
    }

    @Override
    public void setLooping(boolean isLooping) {
        checkDisposed();
        element.setLoop(isLooping);
    }

    @Override
    public boolean isLooping() {
        checkDisposed();
        return element.isLoop();
    }

    @Override
    public void setVolume(float volume) {
        checkDisposed();
        element.setVolume(volume);
    }

    @Override
    public float getVolume() {
        checkDisposed();
        return element.getVolume();
    }

    @Override
    public void setPan(float pan, float volume) {
        checkDisposed();
        element.setVolume(volume);
    }

    @Override
    public void setPosition(float position) {
        checkDisposed();
        element.setCurrentTime(position);
    }

    @Override
    public float getPosition() {
        checkDisposed();
        return (float)element.getCurrentTime();
    }

    @Override
    public void dispose() {
        if (element != null) {
            element.getParentNode().removeChild(element);
            element = null;
        }
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        this.listener = listener;
    }
}
