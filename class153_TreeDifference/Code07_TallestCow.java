package class122;

/**
 * Tallest Cow (POJ 3263)
 * 
 * 题目来源：POJ 3263
 * 题目链接：http://poj.org/problem?id=3263
 * 
 * 题目描述：
 * 有N头牛站成一行，两头牛能够相互看见，当且仅当它们中间的牛身高都比它们矮。
 * 已知最高的牛是第P头，身高为H，还知道R对关系，每对关系表示两头牛可以相互看见。
 * 求每头牛的身高最大可能是多少。
 * 
 * 算法原理：线性差分数组
 * 这是一个线性差分数组的经典应用。对于每对可以相互看见的牛(a,b)，它们之间的所有牛身高都要比它们矮，
 * 也就是区间[a+1, b-1]内的牛身高都要减1。
 * 
 * 差分数组的核心思想：
 * 对于区间[l,r]的修改操作，我们只需要：
 * 1. diff[l] += d
 * 2. diff[r+1] -= d
 * 最后通过前缀和还原原数组。
 * 
 * 在本题中，对于每对关系(a,b)：
 * 1. diff[a+1] -= 1
 * 2. diff[b] += 1
 * 最后通过前缀和计算每头牛的相对身高，然后加上最高身高H得到实际身高。
 * 
 * 时间复杂度分析：
 * - 处理关系：O(R)
 * - 计算前缀和：O(N)
 * 总时间复杂度：O(N + R)
 * 
 * 空间复杂度分析：
 * - 差分数组：O(N)
 * - 去重集合：O(R)
 * 总空间复杂度：O(N + R)
 * 
 * 工程化考量：
 * 1. 使用HashSet进行关系去重，避免重复处理相同的关系
 * 2. 使用StreamTokenizer进行高效输入，处理大量数据时性能优于Scanner
 * 3. 使用PrintWriter进行高效输出，支持缓冲
 * 4. 关系表示采用长整型编码，避免使用Pair类的开销
 * 
 * 最优解分析：
 * 线性差分数组是解决此类区间修改问题的最优解，通过O(1)的操作标记区间修改，
 * 避免了暴力遍历每个区间的所有元素，时间复杂度比暴力方法的O(R*N)有极大提升。
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashSet;
import java.util.Set;

public class Code07_TallestCow {

	/**
	 * 主函数，处理输入输出并调用相应的算法函数
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		// 使用高效的输出方式
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取输入
		in.nextToken();
		int n = (int) in.nval; // 牛的数量
		in.nextToken();
		int p = (int) in.nval; // 最高牛的编号
		in.nextToken();
		int h = (int) in.nval; // 最高牛的身高
		in.nextToken();
		int r = (int) in.nval; // 关系数量
		
		// 差分数组，初始值为0
		// diff[i]表示第i头牛相对身高与前一头牛的差值
		int[] diff = new int[n + 1];
		
		// 用于去重，避免重复处理相同的关系
		// 使用HashSet存储已处理的关系，提高查找效率
		Set<Long> seen = new HashSet<>();
		
		// 处理每对关系
		for (int i = 0; i < r; i++) {
			in.nextToken();
			int a = (int) in.nval;
			in.nextToken();
			int b = (int) in.nval;
			
			// 确保a < b，便于处理
			// 这样可以统一处理逻辑，无论输入顺序如何
			if (a > b) {
				int temp = a;
				a = b;
				b = temp;
			}
			
			// 用一个长整型表示一对关系，用于去重
			// 编码方式：a * (n + 1) + b，确保唯一性
			long pair = (long) a * (n + 1) + b;
			if (seen.contains(pair)) {
				// 如果关系已处理过，则跳过
				continue;
			}
			seen.add(pair);
			
			/**
			 * 差分操作：在区间(a, b)内的牛身高要减1
			 * 即在a+1位置-1，在b位置+1
			 * 这样通过前缀和计算时，区间(a,b)内的元素都会减1
			 */
			diff[a + 1] -= 1;
			diff[b] += 1;
		}
		
		// 通过前缀和计算每头牛的相对身高，然后加上最高身高h得到实际身高
		// height表示当前牛相对最高牛的身高差
		int height = 0;
		for (int i = 1; i <= n; i++) {
			// 累加差分值，得到相对身高
			height += diff[i];
			// 输出实际身高：最高身高 + 相对身高差
			out.println(h + height);
		}
		
		// 确保输出被刷新
		out.flush();
		// 关闭资源
		out.close();
		br.close();
	}
}