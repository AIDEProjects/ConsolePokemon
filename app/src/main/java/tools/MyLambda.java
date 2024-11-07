package tools;

public class MyLambda {
    
    public interface Action<T> {
		public void invoke(T t);
	}
	
	public interface Func<T, V> {
		public V invoke(T t);
	}
    
}
