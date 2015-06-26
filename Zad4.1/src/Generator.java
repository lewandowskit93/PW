import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;


public class Generator implements Runnable {

	protected int starting_permutation_nr;
	protected int current_permutation_nr;
	protected int nr_of_generated;
	protected int end_permutation_nr;
	protected int n;
	protected int[] current_permutation;
	
	public Generator(int starting_permutation_nr, int end_permutation_nr, int n)
	{
		this.starting_permutation_nr=starting_permutation_nr;
		this.end_permutation_nr=end_permutation_nr;
		this.nr_of_generated=0;
		this.current_permutation_nr=starting_permutation_nr;
		this.n=n;
	}
	
	public static int[] getKthPermutation(int k, int n)
	{
		//calculate permutation representation
		int[] perm=new int[n];
		int n_r=k;
		for(int j=0;j<n;++j)
		{
			int f = factorial(n-j-1);
			perm[j]=n_r/f;
			n_r=n_r%f;
		}
		
		//add numbers to permutate
		Set<Integer> numbers = new TreeSet<Integer>(); //TreeSet is ordered
		for(int j=1;j<=n;++j)
		{
			numbers.add(j);
		}
		
		for(int j=0;j<n;++j)
		{
			//get number that is on index position described by permutation representation
			Integer[] numbers_a = (Integer[]) numbers.toArray(new Integer[0]);
			numbers.remove(numbers_a[perm[j]]);
			perm[j]=numbers_a[perm[j]]; // remove number as used already
		}
		
		return perm;
	}
	
	public static int factorial(int x)
	{
		int f=1;
		for(int i=1;i<=x;++i)
		{
			f*=i;
		}
		return f;
	}
	
	synchronized void incNrOfGenerated()
	{
		++nr_of_generated;
	}
	
	synchronized int getNrOfGenerated()
	{
		return nr_of_generated;
	}
	
	synchronized void incCurrentPermutationNr()
	{
		++starting_permutation_nr;
	}
	
	synchronized int getCurrentPermutationNr()
	{
		return starting_permutation_nr;
	}
	
	synchronized void setCurrentPermutation(int[] perm)
	{
		this.current_permutation=perm;
	}
	
	synchronized int[] getCurrentPermutation()
	{
		return Arrays.copyOf(current_permutation, n);
	}
	
	@Override
	public void run() {
		while(getCurrentPermutationNr()<end_permutation_nr)
		{
			try {
				generateNextPermutation();
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public synchronized void generateNextPermutation() throws InterruptedException
	{
		if(current_permutation==null)
		{
			setCurrentPermutation(getKthPermutation(current_permutation_nr, n));
		}
		else
		{
			int k=n-1;
            for (int i=k;i > 0;--i)
            {
                if (current_permutation[i] > current_permutation[i - 1])
                {
                    int j = k;
                    for (;current_permutation[j] < current_permutation[i - 1];--j);
                    int buf = current_permutation[j];
                    current_permutation[j] = current_permutation[i - 1];
                    current_permutation[i - 1] = buf;
                    for (;i < k;++i,--k)
                    {
                        buf = current_permutation[i];
                        current_permutation[i] = current_permutation[k];
                        current_permutation[k] = buf;
                    };
                    break;
                }
            }
		}
		//System.out.println(Arrays.toString(current_permutation));
		incNrOfGenerated();
		incCurrentPermutationNr();
	}

}
