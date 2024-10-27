package consolepokemon.core.turnsystem;

public abstract class TurnSystem
{
	private TurnRole role1, role2;
	private int status=0;
	
	public void startTurn(TurnRole role1, TurnRole role2){
		this.role1 = role1;
		this.role2 = role2;
	}
	
	public void turnLoop(){
		
	}
	
	public void turnCompleted(){
		
	}
	
	public abstract boolean turnPass();
	
	public class TurnRole {
		
	}
}
