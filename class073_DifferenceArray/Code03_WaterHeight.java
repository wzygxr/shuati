package class047;

// 一群人落水后求每个位置的水位高度
// 问题描述比较复杂，见测试链接
// 测试链接 : https://www.luogu.com.cn/problem/P5026
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 相关题目:
// 1. 洛谷 P5026 Lycanthropy
//    链接: https://www.luogu.com.cn/problem/P5026
//    题目描述: 一个朋友落水后会对水面产生影响。当体积为v的朋友在位置x落水时，
//             会产生特定的水位分布。具体影响范围和数值计算方式在题目中有详细说明。
//
// 2. 洛谷 P4231 三步必杀
//    链接: https://www.luogu.com.cn/problem/P4231
//    题目描述: 在区间上添加等差数列，使用二阶差分处理。
//
// 3. Codeforces 296C Greg and Array
//    链接: https://codeforces.com/contest/296/problem/C
//    题目描述: 多层差分操作，需要处理操作的操作。
//
// 偏移量处理核心思想:
// 在某些差分问题中，操作可能会影响数组边界外的位置，为了简化处理，
// 我们引入偏移量OFFSET，将所有操作都映射到正索引范围内。
//
// 对于本题:
// 1. 每个人落水会产生4个等差数列影响区域
// 2. 为了防止负索引出现，使用OFFSET偏移量
// 3. 最终结果数组大小为 OFFSET + MAXN + OFFSET
//
// 时间复杂度分析:
// 每次落水操作: O(1)
// 构造结果数组(两次前缀和): O(m + OFFSET)
// 总时间复杂度: O(n + m + OFFSET)
//
// 空间复杂度分析:
// 需要额外的数组空间: O(OFFSET + MAXN + OFFSET)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_WaterHeight {

	// 题目说了m <= 10^6，代表湖泊宽度
	// 这就是MAXN的含义，湖泊最大宽度的极限
	public static int MAXN = 1000001;

	// 数值保护，因为题目中v最大是10000
	// 所以左侧影响最远的位置到达了x - 3 * v + 1
	// 所以右侧影响最远的位置到达了x + 3 * v - 1
	// x如果是正式的位置(1~m)，那么左、右侧可能超过正式位置差不多30000的规模
	// 这就是OFFSET的含义
	public static int OFFSET = 30001;

	// 湖泊宽度是MAXN，是正式位置的范围
	// 左、右侧可能超过正式位置差不多OFFSET的规模
	// 所以准备一个长度为OFFSET + MAXN + OFFSET的数组
	// 这样一来，左侧影响最远的位置...右侧影响最远的位置，
	// 都可以被arr中的下标表示出来，就省去了很多越界讨论
	// 详细解释看set方法的注释
	public static int[] arr = new int[OFFSET + MAXN + OFFSET];

	public static int n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			// n有多少个人落水，每个人落水意味着四个等差数列操作
			n = (int) in.nval;
			in.nextToken();
			// 一共有多少位置，1~m个位置，最终要打印每个位置的水位
			m = (int) in.nval;
			for (int i = 0, v, x; i < n; i++) {
				in.nextToken(); v = (int) in.nval;
				in.nextToken(); x = (int) in.nval;
				// v体积的朋友，在x处落水，修改差分数组
				fall(v, x);
			}
			// 生成最终的水位数组
			build();
			// 开始收集答案
			// 0...OFFSET这些位置是辅助位置，为了防止越界设计的
			// 从OFFSET+1开始往下数m个，才是正式的位置1...m
			// 打印这些位置，就是返回正式位置1...m的水位
			int start = OFFSET + 1;
			out.print(arr[start++]);
			for (int i = 2; i <= m; i++) {
				out.print(" " + arr[start++]);
			}
			out.println();
		}
		out.flush();
		out.close();
		br.close();
	}

	// 处理一个人落水的情况
	// 当体积为v的朋友在位置x落水时，会产生4个区域的水位变化
	public static void fall(int v, int x) {
		// 区域1: [x - 3*v + 1, x - 2*v] 水位依次增加 1, 2, ..., v
		set(x - 3 * v + 1, x - 2 * v, 1, v, 1);
		// 区域2: [x - 2*v + 1, x] 水位依次减少 (v-1), (v-2), ..., 1, 0
		set(x - 2 * v + 1, x, v - 1, -v, -1);
		// 区域3: [x + 1, x + 2*v] 水位依次减少 -1, -2, ..., -v
		set(x + 1, x + 2 * v, -v + 1, v, 1);
		// 区域4: [x + 2*v + 1, x + 3*v - 1] 水位依次减少 (v-1), (v-2), ..., 1
		set(x + 2 * v + 1, x + 3 * v - 1, v - 1, 1, -1);
	}

	// 在区间[l,r]上加上首项为s、末项为e、公差为d的等差数列
	// 这是等差数列差分的核心操作
	public static void set(int l, int r, int s, int e, int d) {
		// 为了防止x - 3 * v + 1出现负数下标，进而有很多很烦的边界讨论
		// 所以任何位置，都加上一个较大的数字(OFFSET)
		// 这样一来，所有下标就都在0以上了，省去了大量边界讨论
		// 这就是为什么arr在初始化的时候要准备OFFSET + MAXN + OFFSET这么多的空间
		arr[l + OFFSET] += s;
		arr[l + 1 + OFFSET] += d - s;
		arr[r + 1 + OFFSET] -= d + e;
		arr[r + 2 + OFFSET] += e;
	}

	// 通过两次前缀和操作构建最终结果数组
	public static void build() {
		// 第一次前缀和，得到一阶差分的结果
		for (int i = 1; i <= m + OFFSET; i++) {
			arr[i] += arr[i - 1];
		}
		// 第二次前缀和，得到最终结果
		for (int i = 1; i <= m + OFFSET; i++) {
			arr[i] += arr[i - 1];
		}
	}

}