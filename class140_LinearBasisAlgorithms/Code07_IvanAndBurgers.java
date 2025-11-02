package class137;

// Ivan and Burgers
// 给定一个长度为n的数组，有q次询问，每次询问一个区间[l,r]，
// 求在这个区间内选取若干个数能得到的最大异或和。
// 1 <= n, q <= 5 * 10^5
// 0 <= a_i <= 2^31 - 1
// 测试链接 : https://codeforces.com/problemset/problem/1100/F
// 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

import java.io.*;
import java.util.*;

public class Code07_IvanAndBurgers {
    
    public static int MAXN = 500001;
    public static int BIT = 31;
    
    // 原数组
    public static int[] arr = new int[MAXN];
    // 前缀线性基数组
    public static int[][] prefixBasis = new int[MAXN][BIT + 1];
    // 数组长度和询问数
    public static int n, q;
    
    /**
     * 预处理前缀线性基
     * 算法思路：
     * 1. 对于每个位置i，维护从位置1到位置i的所有数构成的线性基
     * 2. 位置i的线性基可以从位置i-1的线性基转移而来
     * 3. 将arr[i]插入到位置i-1的线性基中，得到位置i的线性基
     * 时间复杂度：O(n * BIT)
     * 空间复杂度：O(n * BIT)
     */
    public static void preprocess() {
        // 初始化位置0的线性基为空
        for (int i = 0; i <= BIT; i++) {
            prefixBasis[0][i] = 0;
        }
        
        // 逐个处理每个位置
        for (int i = 1; i <= n; i++) {
            // 复制前一个位置的线性基
            for (int j = 0; j <= BIT; j++) {
                prefixBasis[i][j] = prefixBasis[i - 1][j];
            }
            
            // 将arr[i]插入到当前位置的线性基中
            insert(i, arr[i]);
        }
    }
    
    /**
     * 将数字插入到指定位置的线性基中
     * 算法思路：
     * 1. 从高位到低位扫描
     * 2. 如果当前位为1且线性基中该位为空，则直接插入
     * 3. 否则用线性基中该位的数异或当前数，继续处理
     * @param pos 位置
     * @param num 要插入的数字
     */
    public static void insert(int pos, int num) {
        for (int i = BIT; i >= 0; i--) {
            if (((num >> i) & 1) != 0) {
                if (prefixBasis[pos][i] == 0) {
                    prefixBasis[pos][i] = num;
                    return;
                }
                num ^= prefixBasis[pos][i];
            }
        }
    }
    
    /**
     * 查询区间[l,r]内选取若干个数能得到的最大异或和
     * 算法思路：
     * 1. 利用前缀线性基的性质，区间[l,r]的线性基可以通过prefixBasis[r]和prefixBasis[l-1]计算得到
     * 2. 从高位到低位贪心地选择线性基中的元素来最大化结果
     * 时间复杂度：O(BIT)
     * 空间复杂度：O(BIT)
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间内选取若干个数能得到的最大异或和
     */
    public static int query(int l, int r) {
        // 临时线性基数组
        int[] tempBasis = new int[BIT + 1];
        
        // 复制prefixBasis[r]到临时线性基
        for (int i = 0; i <= BIT; i++) {
            tempBasis[i] = prefixBasis[r][i];
        }
        
        // 用prefixBasis[l-1]消元
        for (int i = BIT; i >= 0; i--) {
            if (prefixBasis[l - 1][i] != 0) {
                int num = prefixBasis[l - 1][i];
                for (int j = BIT; j >= 0; j--) {
                    if (((num >> j) & 1) != 0) {
                        if (tempBasis[j] == 0) {
                            tempBasis[j] = num;
                            break;
                        }
                        num ^= tempBasis[j];
                    }
                }
            }
        }
        
        // 贪心地选择元素来最大化异或和
        int ans = 0;
        for (int i = BIT; i >= 0; i--) {
            ans = Math.max(ans, ans ^ tempBasis[i]);
        }
        
        return ans;
    }
    
    /**
     * 主函数
     * 读取输入数据，预处理前缀线性基，处理询问，输出结果
     */
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        n = Integer.parseInt(br.readLine());
        
        // 读取数组元素
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 预处理前缀线性基
        preprocess();
        
        // 读取询问数
        q = Integer.parseInt(br.readLine());
        
        // 处理每个询问
        for (int i = 1; i <= q; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            out.println(query(l, r));
        }
        
        out.flush();
        out.close();
    }
}