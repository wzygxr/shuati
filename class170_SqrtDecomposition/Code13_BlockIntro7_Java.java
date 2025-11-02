// 数列分块入门7 - Java实现
// 题目来源：LibreOJ #6283 数列分块入门7
// 题目链接：https://loj.ac/p/6283
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间乘法，区间加法，单点询问
// 操作0：区间乘法 [l, r] * c
// 操作1：区间加法 [l, r] + c
// 操作2：单点询问位置loc的值
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护乘法标记和加法标记，实现懒惰传播
// 3. 标记优先级：先乘法后加法，即实际值 = 原值 * mul + add
// 4. 对于区间操作，不完整块下传标记后直接更新，完整块使用标记
// 5. 对于单点查询，根据标记计算实际值
// 时间复杂度：预处理O(n)，区间乘法操作O(√n)，区间加法操作O(√n)，单点查询操作O(1)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

package class172;

import java.io.*;
import java.util.*;

public class Code13_BlockIntro7_Java {
    
    // 最大数组大小
    public static final int MAXN = 100001;
    
    // 原数组
    public static long[] arr = new long[MAXN];
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的乘法标记和加法标记
    // 优先级：先乘法后加法，即实际值 = 原值 * mul + add
    public static long[] mul = new long[MAXN];
    public static long[] add = new long[MAXN];
    
    // 模数
    public static final long MOD = 10007;
    
    /**
     * 构建分块结构
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * @param n 数组长度
     */
    public static void build(int n) {
        // 块大小取sqrt(n)
        blockSize = (int) Math.sqrt(n);
        // 块数量
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 计算每个元素属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 计算每个块的左右边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 初始化标记数组
        for (int i = 1; i <= blockNum; i++) {
            mul[i] = 1;
            add[i] = 0;
        }
    }
    
    /**
     * 下传标记到具体元素（在需要修改具体元素时使用）
     * @param blockId 块编号
     */
    public static void pushDown(int blockId) {
        for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
            arr[i] = (arr[i] * mul[blockId] % MOD + add[blockId]) % MOD;
        }
        mul[blockId] = 1;
        add[blockId] = 0;
    }
    
    /**
     * 区间乘法操作
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @param c 乘数
     */
    public static void multiply(int l, int r, long c) {
        c %= MOD;
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            // 下传标记
            pushDown(belongL);
            // 处理区间内的元素
            for (int i = l; i <= r; i++) {
                arr[i] = arr[i] * c % MOD;
            }
        } else {
            // 处理左端不完整块
            pushDown(belongL);
            for (int i = l; i <= blockRight[belongL]; i++) {
                arr[i] = arr[i] * c % MOD;
            }
            
            // 处理右端不完整块
            pushDown(belongR);
            for (int i = blockLeft[belongR]; i <= r; i++) {
                arr[i] = arr[i] * c % MOD;
            }
            
            // 处理中间的完整块，使用标记
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                mul[i] = mul[i] * c % MOD;
                add[i] = add[i] * c % MOD;
            }
        }
    }
    
    /**
     * 区间加法操作
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @param c 加数
     */
    public static void addOperation(int l, int r, long c) {
        c %= MOD;
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            // 下传标记
            pushDown(belongL);
            // 处理区间内的元素
            for (int i = l; i <= r; i++) {
                arr[i] = (arr[i] + c) % MOD;
            }
        } else {
            // 处理左端不完整块
            pushDown(belongL);
            for (int i = l; i <= blockRight[belongL]; i++) {
                arr[i] = (arr[i] + c) % MOD;
            }
            
            // 处理右端不完整块
            pushDown(belongR);
            for (int i = blockLeft[belongR]; i <= r; i++) {
                arr[i] = (arr[i] + c) % MOD;
            }
            
            // 处理中间的完整块，使用标记
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                add[i] = (add[i] + c) % MOD;
            }
        }
    }
    
    /**
     * 单点查询
     * 时间复杂度：O(1)
     * @param x 查询位置
     * @return 位置x的值
     */
    public static long query(int x) {
        int blockId = belong[x];
        // 根据标记计算实际值
        return (arr[x] * mul[blockId] % MOD + add[blockId]) % MOD;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(elements[i - 1]);
        }
        
        // 构建分块结构
        build(n);
        
        // 处理操作
        for (int i = 1; i <= n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            
            if (op == 0) {
                // 区间乘法操作
                long c = Long.parseLong(operation[3]);
                multiply(l, r, c);
            } else if (op == 1) {
                // 区间加法操作
                long c = Long.parseLong(operation[3]);
                addOperation(l, r, c);
            } else {
                // 单点查询操作
                out.println(query(r));
            }
        }
        
        out.flush();
        out.close();
    }
}