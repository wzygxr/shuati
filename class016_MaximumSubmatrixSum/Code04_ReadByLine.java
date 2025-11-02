package class019;

// 展示acm风格的测试方式
// 测试链接 : https://www.nowcoder.com/exam/test/70070648/detail?pid=27976983
// 其中，7.A+B(7)，就是一个没有给定数据规模，只能按行读数据的例子
// 此时需要自己切分出数据来计算
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 题目描述：
// 计算一系列整数的和
// 输入：
// 多行数据，每行包含若干个整数(可能有正有负)，整数之间用空格分隔
// 输出：
// 对于每一行输入，输出该行所有整数的和
//
// 示例：
// 输入：
// 1 2 3
// 4 5
// -1 0 1
// 输出：
// 6
// 9
// 0
//
// 解题思路：
// 1. 按行读取输入数据
// 2. 使用split方法将每行数据按空格分割成字符串数组
// 3. 将每个字符串转换为整数并累加
// 4. 输出每行的和
//
// 时间复杂度分析：
// - 假设总共有n个数字，时间复杂度为O(n)
//
// 空间复杂度分析：
// - 需要存储每行分割后的字符串数组，最坏情况下为O(m)，其中m为单行最多的数字个数
//
// 适用场景：
// - 输入数据没有明确的数据规模说明
// - 每行数据格式相对简单，可以通过分隔符分割
//
// 优化点：
// - 可以使用更高效的字符串分割方法
// - 可以使用快速输入输出方法提高IO效率

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code04_ReadByLine {

	public static String line;

	public static String[] parts;

	public static int sum;

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while ((line = in.readLine()) != null) {
			parts = line.split(" ");
			sum = 0;
			for (String num : parts) {
				sum += Integer.valueOf(num);
			}
			out.println(sum);
		}
		out.flush();
		in.close();
		out.close();
	}

}