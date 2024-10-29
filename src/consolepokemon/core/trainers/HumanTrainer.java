package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;
import java.util.*;
import consolepokemon.core.trainers.ITrainer.*;

public class HumanTrainer extends Trainer
{
	public Card getCard(){ return ITrainer.Card.Human; }
}
