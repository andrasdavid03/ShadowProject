package shadow.platformer.services.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class LibGdxSoundService implements SoundService {
    private final Map<String, Sound> sounds = new HashMap<>();

    public LibGdxSoundService() {
        sounds.put("notification", Gdx.audio.newSound(Gdx.files.internal("sounds/notification.mp3")));
    }

    @Override
    public void play(String soundName) {
        Sound sound = sounds.get(soundName);
        if (sound != null) sound.play();
    }

}
