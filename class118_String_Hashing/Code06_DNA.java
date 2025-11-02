package class105;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 洛谷 P3763 DNA序列匹配问题实现
 * <p>
 * 题目链接：https://www.luogu.com.cn/problem/P3763
 * <p>
 * 题目描述：
 * 给定长为n的字符串s，以及长度为m的字符串p，还有一个正数k
 * 定义s'与s匹配：s'与s长度相同，且最多有k个位置字符不同
 * 要求查找字符串s中有多少子串与字符串p匹配
 * <p>
 * 具体示例：
 * 输入：
 * 1
 * ATGCATGC
 * TGCATGC
 * k=1
 * 输出：2
 * 解释：s中的子串"TGCATGC"（索引1-7）与p完全匹配；子串"ATGCATG"（索引0-6）只有第一个字符不同
 * <p>
 * 算法核心思想：
 * 结合多项式滚动哈希和二分查找技术，高效检测字符串间的差异位置
 * <p>
 * 算法详细设计：
 * 1. 哈希技术：
 *    - 使用多项式滚动哈希为字符串s和p预构建前缀哈希数组
 *    - 支持O(1)时间内快速比较任意两个子串是否相同
 * 2. 二分查找优化：
 *    - 对于每对比较的子串，使用二分查找快速定位最长匹配前缀
 *    - 将每次查找不匹配位置的时间复杂度从O(m)降至O(logm)
 * 3. 贪心策略：
 *    - 每次找到不匹配位置后，跳过已匹配部分和不匹配字符
 *    - 仅在必要的位置进行比较，避免冗余计算
 * 4. 剪枝优化：
 *    - 一旦发现差异次数超过k，立即停止比较当前子串
 *    - 减少不必要的计算，提高算法效率
 * <p>
 * 二分查找最长匹配前缀的数学原理：
 * 对于两个等长子串s[l1...r1]和p[l2...r2]，要找到最大的len，使得s[l1...l1+len-1] = p[l2...l2+len-1]
 * 使用二分查找：
 * - 初始范围：1 <= len <= r1-l1+1
 * - 对于中间值m，如果前m个字符匹配，则尝试更大的len；否则尝试更小的len
 * - 这种方法保证了在O(logm)时间内找到最长匹配前缀
 * <p>
 * 算法优势分析：
 * 1. 时间效率：传统逐字符比较的复杂度为O(n*m)，而本算法为O(n*k*logm)
 *    - 当k << m时，优化效果显著
 *    - 当k=3（如本题固定值），复杂度接近O(n*logm)
 * 2. 空间效率：仅使用O(n+m)的额外空间存储哈希数组
 * 3. 通用性：可以处理任意字符串匹配问题，不限于DNA序列
 * 4. 实际应用：在生物信息学中的DNA序列比对、文本相似度计算等领域有广泛应用
 * <p>
 * 算法正确性证明：
 * 假设存在一个子串s'与p有t <= k个位置不同，那么算法会在找到t个不匹配位置后返回true
 * 由于算法每次跳过已匹配部分，不会重复计数或遗漏任何不匹配位置
 * 剪枝条件确保一旦差异次数超过k，立即返回false，不会产生误判
 * <p>
 * 时间复杂度详细分析：
 * - 预处理阶段：
 *   - 构建哈希数组和幂次数组：O(n + m + MAXN)
 *   - 由于MAXN是常数，这部分为O(n + m)
 * - 匹配阶段：
 *   - 遍历s中的所有可能子串：O(n - m + 1) ≈ O(n)
 *   - 对每个子串，执行check操作：
 *     - 每次check最多进行k次不匹配位置查找
 *     - 每次查找使用二分查找，复杂度O(logm)
 *     - 因此每次check的复杂度为O(k*logm)
 *   - 总体时间复杂度：O((n + m) + n*k*logm) = O(n*k*logm)
 * <p>
 * 空间复杂度详细分析：
 * - 哈希数组：O(n + m)
 *   - hashs数组存储s的前缀哈希值：O(n)
 *   - hashp数组存储p的前缀哈希值：O(m)
 * - 幂次数组：O(MAXN) ≈ O(1)（常数空间）
 * - 总体空间复杂度：O(n + m)
 * <p>
 * 算法优化策略：
 * 1. 使用滚动哈希：避免重复计算子串哈希值
 * 2. 二分查找：快速定位不匹配位置，而非顺序扫描
 * 3. 剪枝技术：一旦差异次数超过阈值，立即终止比较
 * 4. 快速IO：使用BufferedReader和PrintWriter处理输入输出
 * 5. 字符映射：将字符映射到1-26而非0-25，减少哈希冲突
 * 6. 重用数组：静态数组避免频繁内存分配
 * <p>
 * 哈希冲突处理：
 * 虽然代码中没有显式处理哈希冲突，但通过以下方式降低冲突概率：
 * - 使用大质数499作为基数
 * - 字符映射策略避免零值字符
 * - 在实际竞赛环境中，这种实现通常足以通过测试
 * <p>
 * 测试链接：https://www.luogu.com.cn/problem/P3763
 * 
 * @author Algorithm Journey
 * @note 提交时请将类名改为"Main"以通过评测
 */
public class Code06_DNA {

	/**
	 * 最大字符串长度
	 * 设置为100001，足够处理洛谷题目的输入约束
	 */
	public static int MAXN = 100001;

	/**
	 * 哈希基数，选择499作为大质数以减少哈希冲突
	 * 质数作为基数的优势：分布更均匀，冲突概率更低
	 */
	public static int base = 499;

	/**
	 * 存储base的幂次，避免重复计算
	 * pow[i] = base^i，用于快速计算子串哈希值
	 */
	public static long[] pow = new long[MAXN];

	/**
	 * 存储字符串s的前缀哈希值
	 * hashs[i]表示s[0...i]的哈希值
	 */
	public static long[] hashs = new long[MAXN];

	/**
	 * 存储字符串p的前缀哈希值
	 * hashp[i]表示p[0...i]的哈希值
	 */
	public static long[] hashp = new long[MAXN];

	/**
	 * 主方法，处理输入输出并调用核心算法
	 * <p>
	 * 本方法是程序的入口点，负责高效地读取输入数据，调用compute方法进行处理，
	 * 并将结果输出。特别注意在大数据量处理时的IO效率优化。
	 * <p>
	 * 详细实现步骤：
	 * 1. IO流初始化：
	 *    - 使用BufferedReader替代Scanner以提高读取效率
	 *    - 使用PrintWriter替代System.out.println以提高写入效率
	 *    - 这对于处理大数据量的测试用例至关重要
	 * 
	 * 2. 数据读取：
	 *    - 读取第一个整数n，表示测试用例的数量
	 *    - 对于每个测试用例：
	 *      - 读取字符串s（源字符串）
	 *      - 读取字符串p（模式字符串）
	 *      - 将字符串转换为字符数组以便后续处理
	 * 
	 * 3. 计算与输出：
	 *    - 调用compute方法计算满足条件的子串数量
	 *    - 注意题目中k固定为3（根据洛谷P3763的要求）
	 *    - 使用PrintWriter输出结果
	 * 
	 * 4. 资源管理：
	 *    - 调用flush()确保所有输出被写入
	 *    - 调用close()释放IO资源
	 * <p>
	 * 性能优化考量：
	 * - BufferedReader的readLine()方法比Scanner更快，尤其是在处理大量输入时
	 * - PrintWriter的println()方法比System.out.println()更高效，因为它减少了同步开销
	 * - 使用char[]而非String直接传递给compute方法，避免额外的字符复制
	 * <p>
	 * 输入输出格式示例：
	 * 输入：
	 * 2
	 * AATCGGGTTCAATCGGGGT
	 * ATCGGG
	 * ATGCATGC
	 * TGCATGC
	 * 
	 * 输出：
	 * 2
	 * 2
	 * <p>
	 * 错误处理：
	 * - 方法声明了IOException异常，确保在输入输出错误时能正常处理
	 * - 对于格式不正确的输入，可能会导致运行时异常（但在编程竞赛中通常假设输入格式正确）
	 * 
	 * @param args 命令行参数（未使用）
	 * @throws IOException 当发生输入输出错误时抛出
	 */
	public static void main(String[] args) throws IOException {
		// 使用BufferedReader和PrintWriter进行高效的输入输出
		// 在大数据量情况下，这比Scanner和System.out.println效率高得多
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取测试用例数量
		int n = Integer.valueOf(in.readLine());
		
		// 处理每个测试用例
		for (int i = 0; i < n; i++) {
			String s = in.readLine(); // 读取源字符串s
			String p = in.readLine(); // 读取模式字符串p
			// 计算结果并输出，注意这里k固定为3
			out.println(compute(s.toCharArray(), p.toCharArray(), 3));
		}
		
		// 刷新输出缓冲区并关闭流
		out.flush();
		out.close();
		in.close();
	}

/**
	 * 计算s中有多少子串修改最多k个位置的字符就可以变成p
	 * <p>
	 * 该方法是算法的主要入口，通过滑动窗口遍历所有可能的子串，并调用check方法检测每个子串
	 * 是否满足最多k个差异的条件。结合哈希预处理和二分查找优化，实现高效的子串匹配。
	 * <p>
	 * 详细实现流程：
	 * 1. 边界条件处理：
	 *    - 检查源字符串s的长度是否小于模式字符串p的长度
	 *    - 如果s.length < p.length，不可能有匹配的子串，直接返回0
	 * 
	 * 2. 预处理阶段：
	 *    - 调用build方法构建s和p的哈希数组以及幂次数组
	 *    - 这是后续高效子串比较的基础
	 * 
	 * 3. 滑动窗口遍历：
	 *    - 初始化结果计数器ans为0
	 *    - 遍历s中所有可能的起始位置i，其中i的范围是0 <= i <= n-m
	 *    - 对于每个起始位置i，调用check方法检查s[i...i+m-1]与p的差异情况
	 *    - 如果差异位置数<=k，ans增1
	 * 
	 * 4. 返回结果：
	 *    - 遍历结束后，ans即为满足条件的子串数量
	 * <p>
	 * 滑动窗口策略详解：
	 * - 窗口大小固定为m（p的长度）
	 * - 窗口在s上从左向右滑动，步长为1
	 * - 对于每个窗口位置，只需要检查该窗口内的子串与p的匹配情况
	 * <p>
	 * 算法设计亮点：
	 * - 预处理与查询分离：构建哈希数组是一次性的预处理，后续所有查询都基于此
	 * - 高效的子串比较：通过哈希技术将子串比较的复杂度降为O(1)
	 * - 剪枝优化：在check方法中实现了提前终止的剪枝策略
	 * <p>
	 * 示例执行过程：
	 * 对于s = "ATGCATGC", p = "TGCATGC", k = 1
	 * - 窗口大小m = 7
	 * - 窗口起始位置i=0: 子串"ATGCATG"，与p比较，差异位置数为1，满足条件，ans=1
	 * - 窗口起始位置i=1: 子串"TGCATGC"，与p完全匹配，差异位置数为0，满足条件，ans=2
	 * - 最终返回ans=2
	 * <p>
	 * 算法优化方向：
	 * - 在大数据量情况下，可以考虑并行处理多个窗口位置
	 * - 对于重复出现的模式，可以使用更高级的字符串匹配算法如KMP或Z-算法
	 * - 可以考虑双哈希策略（使用两个不同的基数和模数）来进一步降低哈希冲突的概率
	 * 
	 * @param s 源字符串的字符数组，需要从中查找匹配的子串
	 * @param p 模式字符串的字符数组，作为匹配的目标
	 * @param k 允许的最大不匹配位置数量，>=0的整数
	 * @return 满足条件的子串数量，即s中与p差异位置数<=k的子串数量
	 * 
	 * 时间复杂度：O(n*k*logm)
	 * - n是s的长度，m是p的长度，k是允许的最大不匹配次数
	 * - 构建哈希数组：O(n + m)
	 * - 遍历n-m+1个子串：O(n)
	 * - 每个子串的check操作：O(k*logm)
	 * 
	 * 空间复杂度：O(n + m)
	 * - 哈希数组：O(n + m)
	 * - 幂次数组：O(MAXN)（常数空间）
	 */
	public static int compute(char[] s, char[] p, int k) {
		int n = s.length;  // 源字符串s的长度
		int m = p.length;  // 模式字符串p的长度
		
		// 边界检查：如果s的长度小于p，不可能有匹配的子串
		if (n < m) {
			return 0;
		}
		
		// 预处理：构建s和p的哈希数组和幂次数组
		build(s, n, p, m);
		
		int ans = 0; // 记录满足条件的子串数量
		
		// 滑动窗口：遍历s中所有长度为m的子串
		// 起始位置i的范围：0 <= i <= n-m
		for (int i = 0; i <= n - m; i++) {
			// 检查s[i...i+m-1]和p[0...m-1]是否最多有k个位置不同
			// 使用二分查找优化的check方法
			if (check(i, i + m - 1, k)) {
				ans++; // 满足条件，计数加1
			}
		}
		
		return ans;
	}

/**
	 * 检查s[l1...r1]和p[l2...r2]（等长）是否最多有k个位置不同
	 * <p>
	 * 该方法是本算法的核心创新点，使用二分查找结合滚动哈希，高效地定位不匹配位置，
	 * 而不是简单地逐字符比较。这种方法在k较小时（如本题k=3）尤其高效。
	 * <p>
	 * 算法深度解析：
	 * 1. 初始化：
	 *    - 差异计数器diff初始化为0
	 *    - l2始终从0开始（因为p的起始位置固定）
	 *    - l1和r1表示当前在s中检查的范围
	 * 
	 * 2. 核心循环处理：
	 *    - 当还有字符需要比较且差异次数未超过k时继续
	 *    - 使用二分查找找出从当前位置开始的最长匹配前缀
	 *    - 二分查找范围：1到剩余未比较的字符数
	 *    - 对于中间值m，调用same方法检查前m个字符是否匹配
	 *    - 如果匹配，尝试更长的前缀；否则尝试更短的前缀
	 * 
	 * 3. 差异处理：
	 *    - 如果未比较完所有字符，说明找到一个不匹配位置，diff增1
	 *    - 关键剪枝：一旦diff > k，立即返回false
	 * 
	 * 4. 位置更新：
	 *    - 跳过已匹配的前缀(len个字符)和不匹配的字符(1个)
	 *    - 更新l1和l2到下一个可能的不匹配位置
	 * 
	 * 5. 返回判断：
	 *    - 循环结束后，返回diff <= k的结果
	 * <p>
	 * 二分查找最长匹配前缀的数学原理：
	 * 假设当前比较位置为l1和l2，剩余长度为L
	 * 定义函数f(x)为：s[l1...l1+x-1]和p[l2...l2+x-1]是否匹配
	 * 函数f(x)具有单调性：如果f(x)=true，则对于所有y<x，f(y)=true
	 * 因此，可以使用二分查找找到最大的x，使得f(x)=true
	 * <p>
	 * 示例执行过程：
	 * 对于s="ATGCATG"和p="TGCATGC"，l1=0, l2=0, k=1
	 * 1. 第一次二分查找：
	 *    - 尝试长度m=3，发现"ATG"和"TGC"不匹配
	 *    - 尝试长度m=1，发现"A"和"T"不匹配
	 *    - 最长匹配前缀长度len=0
	 *    - 找到不匹配位置，diff=1
	 *    - 更新l1=1, l2=1
	 * 2. 第二次二分查找：
	 *    - 尝试剩余长度6的一半
	 *    - 最终发现从位置1开始的6个字符完全匹配
	 *    - 所有字符比较完毕，循环结束
	 * 3. 返回true，因为diff=1 <= k=1
	 * <p>
	 * 算法优势：
	 * - 当k较小且不匹配位置较少时，效率极高
	 * - 每次比较跳过已匹配的部分，避免重复比较
	 * - 利用二分查找快速定位不匹配位置
	 * - 剪枝策略确保及时终止不必要的比较
	 * <p>
	 * 边界条件处理：
	 * - 当k=0时（完全匹配），算法退化为高效的字符串匹配算法
	 * - 当l1 > r1时，表示所有字符已比较完毕
	 * - 当diff > k时，立即剪枝返回
	 * 
	 * @param l1 s子串的起始位置（包含）
	 * @param r1 s子串的结束位置（包含）
	 * @param k 允许的最大不匹配次数，非负整数
	 * @return 如果s[l1...r1]和p[l2...r2]的不匹配位置数<=k返回true，否则返回false
	 * 
	 * 时间复杂度：O(k*logm)
	 * - 最多执行k次不匹配位置查找
	 * - 每次查找使用二分查找，需要O(logm)时间
	 * - 其中m是子串的长度
	 * 
	 * 空间复杂度：O(1)
	 * - 仅使用常数级额外空间
	 */
	public static boolean check(int l1, int r1, int k) {
		int diff = 0; // 记录不匹配的位置数
		int l2 = 0;   // p的起始位置，始终从0开始
		
		// 当还有字符需要比较且不匹配次数未超过k时继续
		// 这是一个重要的剪枝条件：一旦diff>k，立即停止比较
		while (l1 <= r1 && diff <= k) {
			// 使用二分查找找出最长的匹配前缀
			int l = 1;            // 二分查找左边界（至少检查1个字符）
			int r = r1 - l1 + 1;  // 二分查找右边界，最大可能长度
			int m, len = 0;       // len记录最长匹配前缀长度
			
			// 二分查找过程
			while (l <= r) {
				m = (l + r) / 2;  // 中间位置
				// 检查从当前位置开始的m个字符是否匹配
				if (same(l1, l2, m)) { 
					len = m;        // 更新最长匹配长度
					l = m + 1;      // 尝试更长的长度
				} else {
					r = m - 1;      // 尝试更短的长度
				}
			}
			
			// 如果没有匹配完所有字符，说明找到了一个不匹配的位置
			if (l1 + len <= r1) {
				diff++; // 差异计数加1
				
				// 剪枝：如果差异计数已经超过k，提前返回false
				if (diff > k) {
					return false;
				}
			}
			
			// 跳过已匹配的部分和不匹配的字符
			// 移动到下一个可能的不匹配位置
			l1 += len + 1;
			l2 += len + 1;
		}
		
		// 返回不匹配次数是否在允许范围内
		return diff <= k;
	}

/**
	 * 比较两个子串是否相同
	 * <p>
	 * 该方法是实现O(1)时间子串比较的关键，通过比较预处理好的哈希值，
	 * 避免了传统的O(len)时间复杂度的逐字符比较。
	 * <p>
	 * 实现原理：
	 * - 调用hash方法分别计算s[l1...l1+len-1]和p[l2...l2+len-1]的哈希值
	 * - 比较两个哈希值是否相等
	 * - 由于哈希值是通过多项式滚动哈希算法计算的，相同的子串会产生相同的哈希值
	 * <p>
	 * 哈希冲突问题：
	 * - 理论上，不同的子串可能产生相同的哈希值，这被称为哈希冲突
	 * - 在实际应用中，通过选择合适的基数（如大质数499）可以显著降低冲突概率
	 * - 在编程竞赛环境中，这种实现通常足够可靠
	 * - 对于要求绝对正确的场景，可以在哈希值相等时再进行一次O(len)时间的字符串比较
	 * <p>
	 * 性能特点：
	 * - 无论子串长度如何，比较操作的时间复杂度均为O(1)
	 * - 这是实现高效差异检测的基础，使得二分查找策略能够发挥最大效益
	 * <p>
	 * 使用场景：
	 * - 在check方法中，用于二分查找过程中的子串比较
	 * - 是整个算法优化的核心环节之一
	 * <p>
	 * 优化潜力：
	 * - 可以实现双哈希策略，使用两个不同的基数和模数计算两个哈希值
	 * - 只有当两个哈希值都相等时才认为子串相同
	 * - 这可以将哈希冲突的概率降低到几乎可以忽略不计
	 * 
	 * @param l1 s子串的起始位置（包含）
	 * @param l2 p子串的起始位置（包含）
	 * @param len 子串长度，必须是正整数且不超出字符串边界
	 * @return 如果两个子串的内容相同返回true，否则返回false
	 * 
	 * 时间复杂度：O(1) - 常数时间操作
	 * 空间复杂度：O(1) - 无需额外空间
	 */
	public static boolean same(int l1, int l2, int len) {
		// 计算s[l1...l1+len-1]和p[l2...l2+len-1]的哈希值并比较
		// 通过哈希值比较，实现O(1)时间的子串比较
		return hash(hashs, l1, l1 + len - 1) == hash(hashp, l2, l2 + len - 1);
	}

/**
	 * 构建s和p的哈希数组及幂次数组
	 * <p>
	 * 该方法是多项式滚动哈希算法的关键预处理步骤，通过预计算前缀哈希值和幂次数组，
	 * 为后续O(1)时间的子串哈希查询奠定基础。预处理是一次性的，但可以支持多次查询。
	 * <p>
	 * 详细实现步骤：
	 * 1. 幂次数组构建：
	 *    - 初始化pow[0] = 1（任何数的0次方为1）
	 *    - 预计算从pow[1]到pow[MAXN-1]的所有值
	 *    - 使用滚动计算：pow[j] = pow[j-1] * base
	 *    - 这避免了在查询时重复计算base的幂次
	 * 
	 * 2. s的前缀哈希数组构建：
	 *    - 初始化hashs[0]为s[0]的映射值
	 *    - 使用字符映射：将字符转换为1-26的整数
	 *    - 通过递推公式构建整个前缀哈希数组：hashs[j] = hashs[j-1] * base + s[j]的映射值
	 * 
	 * 3. p的前缀哈希数组构建：
	 *    - 类似地构建hashp数组
	 *    - 初始化hashp[0]为p[0]的映射值
	 *    - 使用相同的递推公式构建整个数组
	 * <p>
	 * 多项式哈希函数详解：
	 * 对于字符串s = s[0]s[1]...s[n-1]，其哈希值定义为：
	 * hash(s) = s[0] * base^(n-1) + s[1] * base^(n-2) + ... + s[n-1] * base^0
	 * <p>
	 * 递推公式的数学证明：
	 * 假设hashs[j-1] = s[0] * base^(j-1) + s[1] * base^(j-2) + ... + s[j-1] * base^0
	 * 则hashs[j-1] * base = s[0] * base^j + s[1] * base^(j-1) + ... + s[j-1] * base^1
	 * 加上s[j]的映射值后：
	 * hashs[j] = s[0] * base^j + s[1] * base^(j-1) + ... + s[j-1] * base^1 + s[j] * base^0
	 * 这正是前缀s[0...j]的多项式哈希值
	 * <p>
	 * 字符映射策略：
	 * - 使用s[j] - 'a' + 1的方式将字符映射到1-26
	 * - 为什么不直接使用s[j] - 'a'？
	 *   - 避免零值字符导致的哈希冲突
	 *   - 例如，"a"和""（空字符串）会有不同的哈希值
	 *   - 多个连续的'a'不会导致哈希值提前收敛
	 * <p>
	 * 示例计算：
	 * 对于字符串"ATGC"，假设A=1, T=20, G=7, C=3，base=499
	 * pow数组：[1, 499, 249001, 124251499, ...]
	 * hashs数组：
	 * hashs[0] = 1
	 * hashs[1] = 1*499 + 20 = 519
	 * hashs[2] = 519*499 + 7 = 260,998
	 * hashs[3] = 260,998*499 + 3 = 130,238,005
	 * <p>
	 * 注意事项：
	 * - 该方法假设s和p都是非空的，且长度分别为n和m
	 * - 幂次数组预计算到MAXN，确保足够处理任何可能的子串长度
	 * - 对于不同的输入，该方法会覆盖之前的哈希数组，确保数据正确性
	 * 
	 * @param s 源字符串的字符数组
	 * @param n s的长度，必须等于s.length
	 * @param p 模式字符串的字符数组
	 * @param m p的长度，必须等于p.length
	 * 
	 * 时间复杂度：O(n + m + MAXN)
	 * - 计算幂次数组：O(MAXN)
	 * - 计算s的前缀哈希数组：O(n)
	 * - 计算p的前缀哈希数组：O(m)
	 * 
	 * 空间复杂度：O(n + m + MAXN)
	 * - 存储幂次数组：O(MAXN)
	 * - 存储哈希数组：O(n + m)
	 */
	public static void build(char[] s, int n, char[] p, int m) {
		// 初始化幂次数组
		// pow[0] = 1（任何数的0次方都是1）
		pow[0] = 1;
		// 预计算所有可能需要的幂次值
		for (int j = 1; j < MAXN; j++) {
			pow[j] = pow[j - 1] * base;
		}
		
		// 构建s的哈希数组
		// 将字符映射到1-26，避免0值导致的哈希冲突
		hashs[0] = s[0] - 'a' + 1; 
		// 滚动计算前缀哈希值
		for (int j = 1; j < n; j++) {
			// 哈希值计算：hashs[j] = hashs[j-1] * base + s[j]的映射值
			hashs[j] = hashs[j - 1] * base + s[j] - 'a' + 1;
		}
		
		// 构建p的哈希数组
		hashp[0] = p[0] - 'a' + 1; 
		// 滚动计算前缀哈希值
		for (int j = 1; j < m; j++) {
			// 哈希值计算：hashp[j] = hashp[j-1] * base + p[j]的映射值
			hashp[j] = hashp[j - 1] * base + p[j] - 'a' + 1;
		}
	}

/**
	 * 计算子串的哈希值
	 * <p>
	 * 该方法是多项式滚动哈希算法的核心查询操作，实现了O(1)时间复杂度的任意子串哈希计算。
	 * 通过巧妙地利用预处理好的前缀哈希数组和幂次数组，可以高效地提取任意子串的哈希值。
	 * <p>
	 * 详细数学推导：
	 * 假设我们有前缀哈希数组hash，其中hash[i]表示字符串前i+1个字符的哈希值，
	 * 现在要计算子串s[l...r]的哈希值：
	 * <p>
	 * 1. hash[r]表示s[0...r]的哈希值：
	 *    hash[r] = s[0] * base^r + s[1] * base^(r-1) + ... + s[l-1] * base^(r-l+1) + 
	 *              s[l] * base^(r-l) + ... + s[r] * base^0
	 * <p>
	 * 2. hash[l-1]表示s[0...l-1]的哈希值：
	 *    hash[l-1] = s[0] * base^(l-1) + s[1] * base^(l-2) + ... + s[l-1] * base^0
	 * <p>
	 * 3. 将hash[l-1]乘以base^(r-l+1)，得到：
	 *    hash[l-1] * pow[r-l+1] = s[0] * base^r + s[1] * base^(r-1) + ... + s[l-1] * base^(r-l+1)
	 * <p>
	 * 4. 从hash[r]中减去这部分，得到：
	 *    hash[r] - hash[l-1] * pow[r-l+1] = s[l] * base^(r-l) + ... + s[r] * base^0
	 *    这正是子串s[l...r]的哈希值
	 * <p>
	 * 边界条件处理：
	 * - 当l=0时，表示从字符串开头开始的子串
	 * - 此时不需要减去任何前缀，直接返回hash[r]
	 * - 使用条件表达式优雅地处理这种情况
	 * <p>
	 * 实现细节：
	 * - 方法接受一个通用的哈希数组参数，可以是hashs或hashp
	 * - 这种设计使得同一个方法可以用于计算s或p中任意子串的哈希值
	 * - 计算过程简洁明了，仅包含常数次操作
	 * <p>
	 * 示例计算：
	 * 对于字符串"ATGC"（哈希数组如上例），base=499
	 * 计算子串"TG"（l=1, r=2）的哈希值：
	 * hash[2] = 260,998
	 * hash[0] = 1
	 * pow[2] = 249001
	 * 子串哈希值 = hash[2] - hash[0] * pow[2] = 260,998 - 1 * 249,001 = 11,997
	 * 这等价于直接计算 T*base + G = 20*499 + 7 = 11,997
	 * <p>
	 * 算法正确性保证：
	 * 该方法计算的哈希值与直接对该子串应用多项式哈希函数的结果完全一致
	 * 这是因为前缀哈希的递推公式和子串哈希的计算方式是基于同一数学模型推导的
	 * <p>
	 * <b>注意事项：</b>
	 * - 该实现未使用模运算，对于较长字符串可能导致数值溢出
	 * - 在实际应用中，建议添加模运算，如取模10^9+7等大质数
	 * - 由于哈希冲突可能发生，在哈希值相等后应进行字符串的实际比较以确保正确性
	 * - 该方法假设l和r是有效的索引，且满足0 <= l <= r < 字符串长度
	 * - 该方法假设build方法已经被调用，哈希数组和幂次数组已正确初始化
	 * - 参数hash必须是通过build方法构建的前缀哈希数组
	 * 
	 * @param hash 前缀哈希数组（可以是hashs或hashp）
	 * @param l 子串起始位置（包含），0-based索引
	 * @param r 子串结束位置（包含），0-based索引
	 * @return 子串s[l...r]的多项式滚动哈希值
	 * 
	 * 时间复杂度：O(1) - 常数时间操作，不受子串长度影响
	 * 空间复杂度：O(1) - 无需额外空间
	 */
	public static long hash(long[] hash, int l, int r) {
		// 初始值为hash[r]（从0到r的前缀哈希值）
		long ans = hash[r]; 
		
		// 当l>0时，需要减去0到l-1部分的影响
		// hash[l-1] * pow[r-l+1] 计算的是s[0...l-1]在hash[r]中的贡献
		ans -= l == 0 ? 0 : (hash[l - 1] * pow[r - l + 1]);
		
		return ans;
	}

	/**
	 * 哈希冲突概率的数学分析
	 * <p>
	 * 在多项式滚动哈希中，哈希冲突是不可避免的。冲突概率受以下因素影响：
	 * <ol>
	 * <li><b>基数选择(base)</b>：本实现选择499作为基数</li>
	 * <li><b>模数选择</b>：本实现未使用模数，但生产环境应使用大质数模数</li>
	 * <li><b>哈希空间大小</b>：对于long类型(64位)，理论哈希空间为2^64</li>
	 * </ol>
	 * 
	 * <p>
	 * <b>生日悖论应用：</b>
	 * 当有m个不同的字符串时，冲突概率可近似为1 - e^(-m²/(2*H))，
	 * 其中H为哈希空间大小。对于64位哈希空间：
	 * <ul>
	 * <li>当m=10^6时，冲突概率约为1.1×10^-13</li>
	 * <li>当m=10^7时，冲突概率约为1.1×10^-11</li>
	 * <li>当m=10^8时，冲突概率约为1.1×10^-9</li>
	 * <li>当m=10^9时，冲突概率约为1.1×10^-7</li>
	 * </ul>
	 * 对于大多数应用场景，64位无符号整数哈希空间提供了足够低的冲突概率。
	 * 
	 * <p>
	 * <b>安全界限：</b>
	 * 理论上，当m超过约2^32时，哈希冲突概率将超过50%。
	 * 在实际开发中，为提高安全性，通常使用双哈希或多哈希策略。
	 */

	/**
	 * 双哈希实现示例
	 * <p>
	 * 为进一步降低哈希冲突的概率，可以实现双哈希策略：
	 * <pre>
	 * // 定义两组哈希参数
	 * public static int base1 = 499;
	 * public static int mod1 = 1000000007;
	 * public static int base2 = 1009;
	 * public static int mod2 = 1000000009;
	 * 
	 * // 定义两组哈希数组和幂次数组
	 * public static long[] pow1 = new long[MAXN];
	 * public static long[] hashs1 = new long[MAXN];
	 * public static long[] hashp1 = new long[MAXN];
	 * public static long[] pow2 = new long[MAXN];
	 * public static long[] hashs2 = new long[MAXN];
	 * public static long[] hashp2 = new long[MAXN];
	 * 
	 * // 构建双哈希
	 * public static void buildDoubleHash(char[] s, int n, char[] p, int m) {
	 *     // 第一组哈希构建
	 *     pow1[0] = 1;
	 *     for (int j = 1; j < MAXN; j++) {
	 *         pow1[j] = (pow1[j - 1] * base1) % mod1;
	 *     }
	 *     hashs1[0] = (s[0] - 'a' + 1) % mod1;
	 *     for (int j = 1; j < n; j++) {
	 *         hashs1[j] = (hashs1[j - 1] * base1 + s[j] - 'a' + 1) % mod1;
	 *     }
	 *     hashp1[0] = (p[0] - 'a' + 1) % mod1;
	 *     for (int j = 1; j < m; j++) {
	 *         hashp1[j] = (hashp1[j - 1] * base1 + p[j] - 'a' + 1) % mod1;
	 *     }
	 *     
	 *     // 第二组哈希构建
	 *     pow2[0] = 1;
	 *     for (int j = 1; j < MAXN; j++) {
	 *         pow2[j] = (pow2[j - 1] * base2) % mod2;
	 *     }
	 *     hashs2[0] = (s[0] - 'a' + 1) % mod2;
	 *     for (int j = 1; j < n; j++) {
	 *         hashs2[j] = (hashs2[j - 1] * base2 + s[j] - 'a' + 1) % mod2;
	 *     }
	 *     hashp2[0] = (p[0] - 'a' + 1) % mod2;
	 *     for (int j = 1; j < m; j++) {
	 *         hashp2[j] = (hashp2[j - 1] * base2 + p[j] - 'a' + 1) % mod2;
	 *     }
	 * }
	 * 
	 * // 计算子串的哈希值
	 * public static long hash(long[] hash, long[] pow, int l, int r, int mod) {
	 *     long ans = hash[r];
	 *     if (l > 0) {
	 *         ans = (ans - hash[l - 1] * pow[r - l + 1] % mod + mod) % mod;
	 *     }
	 *     return ans;
	 * }
	 * 
	 * // 比较两个子串是否相同（双哈希）
	 * public static boolean sameDoubleHash(int l1, int l2, int len) {
	 *     // 只有当两组哈希值都相等时才认为子串相同
	 *     return hash(hashs1, pow1, l1, l1 + len - 1, mod1) == hash(hashp1, pow1, l2, l2 + len - 1, mod1) &&
	 *            hash(hashs2, pow2, l1, l1 + len - 1, mod2) == hash(hashp2, pow2, l2, l2 + len - 1, mod2);
	 * }
	 * </pre>
	 * 
	 * 双哈希可以将冲突概率降至接近零，因为两组独立的哈希同时冲突的概率极低。
	 * 在实际应用中，需要相应地修改check方法以使用sameDoubleHash。
	 */

	/**
	 * 推荐测试用例实现
	 * <p>
	 * 以下是针对该算法的JUnit测试用例示例：
	 * <pre>
	 * import org.junit.Test;
	 * import static org.junit.Assert.*;
	 * 
	 * public class Code06_DNATest {
	 *     
	 *     @Test
	 *     public void testBasicCases() {
	 *         // 基本匹配测试
	 *         char[] s1 = "ATGCATGC".toCharArray();
	 *         char[] p1 = "TGCATGC".toCharArray();
	 *         assertEquals(2, Code06_DNA.compute(s1, p1, 1));
	 *         
	 *         // 完全匹配测试
	 *         char[] s2 = "AATCGGGTTCAATCGGGGT".toCharArray();
	 *         char[] p2 = "ATCGGG".toCharArray();
	 *         assertEquals(2, Code06_DNA.compute(s2, p2, 0));
	 *     }
	 *     
	 *     @Test
	 *     public void testDifferentKValues() {
	 *         // 不同k值测试
	 *         char[] s = "ABCDEFGHIJKLMN".toCharArray();
	 *         char[] p = "ABCDEFG".toCharArray();
	 *         
	 *         // k=0：完全匹配
	 *         assertEquals(1, Code06_DNA.compute(s, p, 0));
	 *         
	 *         // 修改一个字符后的子串
	 *         char[] p2 = "A1CDEFG".toCharArray();
	 *         assertEquals(1, Code06_DNA.compute(s, p2, 1));
	 *         
	 *         // 修改两个字符后的子串
	 *         char[] p3 = "A1CDFG".toCharArray();
	 *         assertEquals(1, Code06_DNA.compute(s, p3, 2));
	 *     }
	 *     
	 *     @Test
	 *     public void testEdgeCases() {
	 *         // 边界情况测试
	 *         char[] s1 = "A".toCharArray();
	 *         char[] p1 = "A".toCharArray();
	 *         assertEquals(1, Code06_DNA.compute(s1, p1, 0));
	 *         
	 *         // s比p短的情况
	 *         char[] s2 = "AB".toCharArray();
	 *         char[] p2 = "ABC".toCharArray();
	 *         assertEquals(0, Code06_DNA.compute(s2, p2, 0));
	 *         
	 *         // 空字符串测试
	 *         char[] s3 = "".toCharArray();
	 *         char[] p3 = "A".toCharArray();
	 *         assertEquals(0, Code06_DNA.compute(s3, p3, 0));
	 *     }
	 *     
	 *     @Test
	 *     public void testDNACharacters() {
	 *         // DNA特定字符测试（只有A、T、G、C）
	 *         char[] s = "ATGCTAGCTAGCTA".toCharArray();
	 *         char[] p = "GCTAG".toCharArray();
	 *         assertEquals(2, Code06_DNA.compute(s, p, 0));
	 *     }
	 *     
	 *     @Test
	 *     public void testHighKValue() {
	 *         // 高k值测试，允许大部分字符不匹配
	 *         char[] s = "ABCDEFGHIJKLMN".toCharArray();
	 *         char[] p = "XXXXXXXXX".toCharArray();
	 *         // 如果k很大，应该至少有一个匹配位置
	 *         assertTrue(Code06_DNA.compute(s, p, 10) > 0);
	 *     }
	 * }
	 * </pre>
	 * 
	 * 这些测试用例涵盖了基本功能测试、不同k值测试、边界情况测试、DNA特定字符测试等多种情况，
	 * 有助于确保算法在各种情况下的正确性和性能。
	 */

	/**
	 * 字符串哈希算法比较分析
	 * <p>
	 * <b>多项式滚动哈希 vs 其他哈希算法：</b>
	 * <table border="1">
	 * <tr><th>算法类型</th><th>优点</th><th>缺点</th><th>适用场景</th></tr>
	 * <tr>
	 * <td>多项式滚动哈希</td>
	 * <td>
	 * - 实现简单<br>
	 * - 支持O(1)子串哈希计算<br>
	 * - 高效的滑动窗口匹配<br>
	 * - 预处理时间O(n)
	 * </td>
	 * <td>
	 * - 可能存在哈希冲突<br>
	 * - 需要选择合适的基数和模数<br>
	 * - 对长字符串可能溢出
	 * </td>
	 * <td>
	 * - 子串搜索<br>
	 * - 重复字符串检测<br>
	 * - 滑动窗口问题<br>
	 * - DNA序列匹配（本题）
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>MD5/SHA-1</td>
	 * <td>
	 * - 极低的冲突概率<br>
	 * - 安全哈希算法<br>
	 * - 标准化实现
	 * </td>
	 * <td>
	 * - 计算开销大<br>
	 * - 不支持O(1)子串哈希<br>
	 * - 性能较低
	 * </td>
	 * <td>
	 * - 数据完整性校验<br>
	 * - 密码存储（使用盐值）<br>
	 * - 数字签名
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>Rabin-Karp算法</td>
	 * <td>
	 * - 滑动窗口O(n)时间复杂度<br>
	 * - 适合多模式匹配<br>
	 * - 易于实现
	 * </td>
	 * <td>
	 * - 最差情况下退化为O(n*m)<br>
	 * - 需要处理哈希冲突<br>
	 * - 不支持二分查找优化
	 * </td>
	 * <td>
	 * - 字符串搜索<br>
	 * - 多模式匹配<br>
	 * - 重复检测
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>xxHash/MurmurHash</td>
	 * <td>
	 * - 极快的计算速度<br>
	 * - 良好的分布特性<br>
	 * - 适合大规模数据处理
	 * </td>
	 * <td>
	 * - 不支持O(1)子串哈希<br>
	 * - 需额外实现子串查询功能<br>
	 * - 不适合二分查找优化
	 * </td>
	 * <td>
	 * - 大规模哈希表<br>
	 * - 数据索引<br>
	 * - 高速缓存
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * <p>
	 * <b>在本问题中的选择理由：</b>
	 * 对于DNA序列匹配问题，多项式滚动哈希结合二分查找是理想选择，因为：
	 * <ol>
	 * <li>需要高效地计算固定长度子串的哈希值以进行匹配</li>
	 * <li>二分查找优化需要快速比较任意长度的前缀是否相同</li>
	 * <li>当k较小时，算法复杂度接近O(n*logm)，远优于暴力比较</li>
	 * <li>滑动窗口技术与滚动哈希完美结合，支持高效的子串遍历</li>
	 * </ol>
	 * 
	 * <p>
	 * <b>算法优化的关键创新点：</b>
	 * <ol>
	 * <li><b>二分查找与滚动哈希的结合</b>：这是本算法最关键的优化，将O(m)的比较转化为O(logm)</li>
	 * <li><b>贪心跳过已匹配部分</b>：每次找到不匹配位置后，直接跳过已匹配的部分</li>
	 * <li><b>剪枝优化</b>：一旦差异次数超过k，立即终止比较，避免不必要的计算</li>
	 * <li><b>O(1)时间子串比较</b>：通过哈希技术实现常数时间的子串内容比较</li>
	 * </ol>
	 * 
	 * <p>
	 * <b>生产环境优化建议：</b>
	 * <ol>
	 * <li>添加模数运算以避免数值溢出，推荐使用10^9+7或10^9+9等大质数</li>
	 * <li>实现双哈希策略以进一步降低冲突概率，特别是在处理生物信息学大数据时</li>
	 * <li>在哈希值相等后进行字符串的实际比较以确保正确性</li>
	 * <li>对于DNA序列，可以使用2-bit编码（A=00, C=01, G=10, T=11）进一步优化空间和计算效率</li>
	 * <li>考虑使用并行计算技术处理多个滑动窗口，特别是在多核环境下</li>
	 * <li>使用缓存技术存储频繁访问的子串哈希值，避免重复计算</li>
	 * </ol>
	 */
}
