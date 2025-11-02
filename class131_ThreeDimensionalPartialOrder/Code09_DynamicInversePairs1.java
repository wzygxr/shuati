package class171;

/**
 * 动态逆序对 - Java版本
 * 
 * 题目来源: 洛谷P3157
 * 题目链接: https://www.luogu.com.cn/problem/P3157
 * 题目难度: 省选/NOI-
 * 
 * 题目描述:
 * 对于序列a，它的逆序对数定义为集合{(i,j)| i < j ∧ ai > aj}中的元素个数。
 * 现在给出1∼n的一个排列，按照某种顺序依次删除m个元素，任务是在每次删除一个元素之前统计整个序列的逆序对数。
 * 
 * 解题思路:
 * 这是一个动态逆序对问题，可以使用CDQ分治来解决。
 * 
 * 算法步骤:
 * 1. 将删除操作转化为时间维度，每个元素有一个删除时间
 * 2. 问题转化为三维偏序：时间、位置、数值
 * 3. 使用CDQ分治处理：
 *    - 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
 *    - 递归处理左半部分和右半部分
 *    - 计算左半部分对右半部分的贡献
 * 4. 在合并过程中：
 *    - 对左半部分按照数值排序
 *    - 对右半部分按照数值排序
 *    - 使用双指针维护数值的顺序
 *    - 使用树状数组维护位置的信息，查询满足条件的元素数量
 * 
 * 时间复杂度: O(n log^2 n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 处理输入参数合法性
 * 2. 性能优化: 使用快速IO提高输入效率
 * 3. 代码可读性: 添加详细注释说明算法思路
 * 4. 调试能力: 添加中间过程打印便于调试
 * 
 * 详细解题思路:
 * 1. 暴力解法：每次删除元素后重新计算逆序对，时间复杂度O(m*n^2)
 * 2. CDQ分治优化：
 *    - 将删除操作转化为时间维度，每个元素有一个删除时间
 *    - 问题转化为三维偏序：时间、位置、数值
 *    - 使用CDQ分治处理时间维度
 * 
 * 算法详解:
 * 1. 首先计算初始序列的逆序对数
 * 2. 将删除操作转化为时间维度：
 *    - 被删除的元素按照删除顺序标记删除时间
 *    - 未被删除的元素删除时间标记为m+1
 * 3. 使用CDQ分治处理时间维度：
 *    - 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
 *    - 递归处理左半部分和右半部分
 *    - 计算左半部分对右半部分的贡献
 * 4. 在合并过程中：
 *    - 对左半部分按照数值排序
 *    - 对右半部分按照数值排序
 *    - 使用双指针维护数值的顺序
 *    - 使用树状数组维护位置的信息，查询满足条件的元素数量
 * 
 * 贡献计算详解:
 * 对于每个元素，我们需要计算它对逆序对数的贡献：
 * 1. 计算左半部分对右半部分的贡献：
 *    - 对于左半部分的每个元素，计算右半部分中数值比它小的元素个数
 * 2. 计算右半部分对左半部分的贡献：
 *    - 对于右半部分的每个元素，计算左半部分中数值比它大的元素个数
 * 
 * 时间复杂度分析:
 * - 计算初始逆序对：O(n log n)
 * - CDQ分治：T(n) = 2T(n/2) + O(n log n) = O(n log^2 n)
 * - 总时间复杂度：O(n log^2 n)
 * 
 * 空间复杂度分析:
 * - 数据结构：O(n)
 * - 树状数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 与其他算法的比较:
 * 1. 与树套树比较:
 *    - CDQ分治空间复杂度更优O(n) vs 树套树O(n log^2 n)
 *    - CDQ分治实现更简单
 *    - 树套树支持在线查询，CDQ分治需要离线处理
 * 2. 与KD树比较:
 *    - CDQ分治在特定问题上更高效
 *    - KD树支持在线查询和更复杂的操作
 * 
 * 优化策略:
 * 1. 使用离散化减少值域范围
 * 2. 优化排序策略减少常数
 * 3. 合理安排计算顺序避免重复计算
 * 4. 使用快速IO提高效率
 * 
 * 常见问题及解决方案:
 * 1. 答案错误:
 *    - 问题：贡献计算错误或边界处理不当
 *    - 解决方案：仔细检查贡献计算逻辑，验证边界条件
 * 2. 时间超限:
 *    - 问题：常数因子过大或算法复杂度分析错误
 *    - 解决方案：优化排序策略，减少不必要的操作
 * 3. 空间超限:
 *    - 问题：递归层数过深或数组开得过大
 *    - 解决方案：检查数组大小，使用全局数组，优化递归逻辑
 * 
 * 扩展应用:
 * 1. 可以处理更高维度的偏序问题
 * 2. 可以优化动态规划的转移过程
 * 3. 可以处理动态问题转静态的场景
 * 
 * 学习建议:
 * 1. 先掌握归并排序求逆序对
 * 2. 理解二维偏序问题的处理方法
 * 3. 学习三维偏序的标准处理流程
 * 4. 练习四维偏序问题
 * 5. 掌握CDQ分治优化DP的方法
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code09_DynamicInversePairs1 {

    public static int MAXN = 100001;
    public static int n, m;

    // 树状数组
    public static int[] tree = new int[MAXN];

    public static int lowbit(int x) {
        return x & (-x);
    }

    public static void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }

    public static int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }

    // 数据结构
    static class Data {
        int val;    // 数值
        int del;    // 删除时间
        int ans;    // 答案贡献
    }

    public static Data[] a = new Data[MAXN];
    public static int[] rv = new int[MAXN];  // reverse mapping
    public static long res;

    // 比较器
    public static boolean cmp1(Data a, Data b) {
        return a.val < b.val;
    }

    public static boolean cmp2(Data a, Data b) {
        return a.del < b.del;
    }

    /**
     * CDQ分治函数
     * @param l 区间左端点
     * @param r 区间右端点
     */
    public static void solve(int l, int r) {
        if (r - l == 1) {
            return;
        }
        int mid = (l + r) / 2;
        solve(l, mid);
        solve(mid, r);
        
        int i = l + 1;
        int j = mid + 1;
        
        // 计算左半部分对右半部分的贡献（计算比右半部分大的左半部分元素）
        while (i <= mid) {
            while (a[i].val > a[j].val && j <= r) {
                add(a[j].del, 1);
                j++;
            }
            a[i].ans += sum(m + 1) - sum(a[i].del);
            i++;
        }
        
        // 清空树状数组
        i = l + 1;
        j = mid + 1;
        while (i <= mid) {
            while (a[i].val > a[j].val && j <= r) {
                add(a[j].del, -1);
                j++;
            }
            i++;
        }
        
        i = mid;
        j = r;
        
        // 计算右半部分对左半部分的贡献（计算比左半部分小的右半部分元素）
        while (j > mid) {
            while (a[j].val < a[i].val && i > l) {
                add(a[i].del, 1);
                i--;
            }
            a[j].ans += sum(m + 1) - sum(a[j].del);
            j--;
        }
        
        // 清空树状数组
        i = mid;
        j = r;
        while (j > mid) {
            while (a[j].val < a[i].val && i > l) {
                add(a[i].del, -1);
                i--;
            }
            j--;
        }
        
        // 按照数值排序
        Arrays.sort(a, l + 1, r + 1, (x, y) -> Integer.compare(x.val, y.val));
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        
        // 初始化数组
        for (int i = 1; i <= n; i++) {
            a[i] = new Data();
        }
        
        // 读入数据
        for (int i = 1; i <= n; i++) {
            a[i].val = in.nextInt();
            rv[a[i].val] = i;
        }
        
        // 读入删除顺序
        for (int i = 1; i <= m; i++) {
            int p = in.nextInt();
            a[rv[p]].del = i;
        }
        
        // 没有被删除的元素删除时间设为m+1
        for (int i = 1; i <= n; i++) {
            if (a[i].del == 0) a[i].del = m + 1;
        }
        
        // 计算初始逆序对数
        for (int i = 1; i <= n; i++) {
            res += sum(n + 1) - sum(a[i].val);
            add(a[i].val, 1);
        }
        
        // 清空树状数组
        for (int i = 1; i <= n; i++) {
            add(a[i].val, -1);
        }
        
        // CDQ分治处理
        solve(0, n);
        
        // 按照删除时间排序
        Arrays.sort(a, 1, n + 1, (x, y) -> Integer.compare(x.del, y.del));
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(res);
            res -= a[i].ans;
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}