// 数列分块入门4 - Java实现
// 题目来源：LibreOJ #6280 数列分块入门4
// 题目链接：https://loj.ac/p/6280
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和
// 操作0：区间加法 [l, r] + c
// 操作1：区间求和 [l, r]
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护元素和，用于快速计算区间和
// 3. 对于区间加法操作，不完整块直接更新并重新计算块和，完整块使用懒惰标记并直接更新块和
// 4. 对于查询操作，不完整块直接遍历，完整块直接使用块和计算
// 时间复杂度：预处理O(n)，区间加法操作O(√n)，区间求和操作O(√n)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 5. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

package class172;

import java.io.*;
import java.util.*;

public class Code10_BlockIntro4_Java {
    
    // 最大数组大小
    public static final int MAXN = 500001;
    
    // 原数组
    public static long[] arr = new long[MAXN];
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的懒惰标记（区间加法标记）
    public static long[] lazy = new long[MAXN];
    
    // 每个块的元素和
    public static long[] sum = new long[MAXN];
    
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
        
        // 计算每个块的元素和
        for (int i = 1; i <= blockNum; i++) {
            sum[i] = 0;
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                sum[i] += arr[j];
            }
        }
    }
    
    /**
     * 区间加法操作
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @param c 加的值
     */
    public static void add(int l, int r, long c) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                arr[i] += c;
            }
            // 更新该块的元素和
            for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
                sum[belongL] = sum[belongL] - (arr[i] - c) + arr[i];
            }
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                arr[i] += c;
            }
            // 更新该块的元素和
            for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
                sum[belongL] = sum[belongL] - (arr[i] - c) + arr[i];
            }
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                arr[i] += c;
            }
            // 更新该块的元素和
            for (int i = blockLeft[belongR]; i <= blockRight[belongR]; i++) {
                sum[belongR] = sum[belongR] - (arr[i] - c) + arr[i];
            }
            
            // 处理中间的完整块，使用懒惰标记
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                lazy[i] += c;
                // 直接更新块的元素和
                sum[i] += c * (blockRight[i] - blockLeft[i] + 1);
            }
        }
    }
    
    /**
     * 查询区间和
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间和
     */
    public static long query(int l, int r) {
        long result = 0;
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                result += arr[i] + lazy[belong[i]];
            }
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                result += arr[i] + lazy[belong[i]];
            }
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                result += arr[i] + lazy[belong[i]];
            }
            
            // 处理中间的完整块，直接使用块的元素和
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                result += sum[i] + lazy[i] * (blockRight[i] - blockLeft[i] + 1);
            }
        }
        
        return result;
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
                // 区间加法操作
                long c = Long.parseLong(operation[3]);
                add(l, r, c);
            } else {
                // 区间求和操作
                out.println(query(l, r));
            }
        }
        
        out.flush();
        out.close();
    }
}