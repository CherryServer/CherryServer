package delta.cion.tokyo.test_plugin.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.WrittenBookContent;

import java.util.ArrayList;
import java.util.List;

public class InfoBook {

	private static final List<Component> LIST = new ArrayList<>();

	public static void getBook(Player player) {
		ItemStack book = ItemStack.of(Material.WRITTEN_BOOK)
			.with(DataComponents.WRITTEN_BOOK_CONTENT,
				new WrittenBookContent("-==[ Server info ]==-", "Nionim", getPages()));

		player.getInventory().addItemStack(book);
	}

	private static List<Component> getPages() {
		LIST.add(Component.text("Это просто тестовый сервер."));
		LIST.add(Component.text("Будет что-то типо DND, но я не уверен..."));
		return LIST;
	}

}
