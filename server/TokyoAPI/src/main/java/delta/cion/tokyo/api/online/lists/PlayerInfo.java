package delta.cion.tokyo.api.online.lists;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerInfo (String name, UUID uuid, LocalDateTime date) { }
