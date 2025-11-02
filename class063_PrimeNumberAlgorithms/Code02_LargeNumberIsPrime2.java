package class097;

// 判断较大的数字是否是质数(Miller-Rabin测试)
// 测试链接 : https://www.luogu.com.cn/problem/U148828
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
// 本文件可以搞定任意范围数字的质数检查，时间复杂度O(s * (logn)的三次方)
// 为什么不自己写，为什么要用BigInteger中的isProbablePrime方法
// 原因在于long类型位数不够，乘法同余的时候会溢出，课上已经做了说明

// 相关题目：
// 1. POJ 1811 Prime Test
//    链接：http://poj.org/problem?id=1811
//    题目描述：给定一个大整数(2 <= N < 2^54)，判断它是否为素数，如果不是输出最小质因子
// 2. Luogu U148828 大数质数判断
//    链接：https://www.luogu.com.cn/problem/U148828
//    题目描述：判断给定的大整数是否为质数
// 3. Codeforces 679A Bear and Prime 100 (交互题)
//    链接：https://codeforces.com/problemset/problem/679/A
//    题目描述：系统想了一个2到100之间的数，你需要通过最多20次询问判断这个数是否为质数

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;

public class Code02_LargeNumberIsPrime2 {

	// 测试次数，次数越多失误率越低，但速度也越慢
	// 在实际应用中，需要在准确性和性能之间找到平衡点
	public static int s = 10;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		int t = Integer.valueOf(br.readLine());
		for (int i = 0; i < t; i++) {
			BigInteger n = new BigInteger(br.readLine());
			// isProbablePrime方法包含MillerRabin和LucasLehmer测试
			// 给定测试次数s即可
			// 该方法返回false表示肯定不是质数，返回true表示可能是质数
			// 误判概率不超过(1/2)^s
			out.println(n.isProbablePrime(s) ? "Yes" : "No");
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * BigInteger.isProbablePrime方法说明：
	 * 1. 该方法结合了Miller-Rabin测试和Lucas-Lehmer测试
	 * 2. 参数certainty表示测试的可信度，值越大误判率越低
	 * 3. 如果返回false，表示该数肯定不是质数
	 * 4. 如果返回true，表示该数很可能是质数，误判概率不超过(1/2)^certainty
	 * 
	 * 工程化考虑：
	 * 1. 对于大数质数判断，使用现成的库函数可以避免实现复杂度
	 * 2. 需要根据实际场景选择合适的certainty值
	 * 3. 在高安全性要求的场景（如密码学），应使用更高的certainty值
	 * 4. 在性能敏感的场景，可以适当降低certainty值
	 */
}