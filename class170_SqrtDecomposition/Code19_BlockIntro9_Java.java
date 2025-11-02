// 数列分块入门9 - Java实现
// 题目来源：LibreOJ #6285 数列分块入门9
// 题目链接：https://loj.ac/p/6285
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及询问区间的最小众数
// 操作：查询区间[l, r]的最小众数（如果有多个出现次数相同，则取最小的）
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 预处理每个块区间[i,j]的最小众数，存储在f[i][j]中
// 3. 对于查询操作，如果区间跨越多个块，则利用预处理结果和暴力统计边界块
// 4. 最小众数定义：出现次数最多，相同出现次数时取最小值
// 时间复杂度：预处理O(n√n)，查询操作O(√n)
// 空间复杂度：O(n + √n * √n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 7. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 8. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284

package class172;

import java.io.*;
import java.util.*;

public class Code19_BlockIntro9_Java {
    
    // 最大数组大小
    public static final int MAXN = 100005;
    
    // 原数组
    public static int[] arr = new int[MAXN];
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 预处理数组f[i][j]表示第i块到第j块的最小众数
    public static int[][] f = new int[505][505];
    
    // 计数数组
    public static int[] cnt = new int[MAXN];
    
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
        
        // 预处理f数组
        preprocess(n);
    }
    
    /**
     * 预处理f数组
     * 时间复杂度：O(n*√n)
     * @param n 数组长度
     */
    public static void preprocess(int n) {
        for (int i = 1; i <= blockNum; i++) {
            // 清空计数数组
            Arrays.fill(cnt, 0);
            
            int maxCnt = 0;
            int minMode = Integer.MAX_VALUE;
            
            for (int j = blockLeft[i]; j <= n; j++) {
                cnt[arr[j]]++;
                
                // 更新众数
                if (cnt[arr[j]] > maxCnt || (cnt[arr[j]] == maxCnt && arr[j] < minMode)) {
                    maxCnt = cnt[arr[j]];
                    minMode = arr[j];
                }
                
                // 记录第i块到第belong[j]块的最小众数
                f[i][belong[j]] = minMode;
            }
        }
    }
    
    /**
     * 查询区间[l, r]的最小众数
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间[l, r]的最小众数
     */
    public static int query(int l, int r) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 清空计数数组
        Arrays.fill(cnt, 0);
        
        int maxCnt = 0;
        int minMode = Integer.MAX_VALUE;
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                cnt[arr[i]]++;
                
                // 更新众数
                if (cnt[arr[i]] > maxCnt || (cnt[arr[i]] == maxCnt && arr[i] < minMode)) {
                    maxCnt = cnt[arr[i]];
                    minMode = arr[i];
                }
            }
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                cnt[arr[i]]++;
                
                // 更新众数
                if (cnt[arr[i]] > maxCnt || (cnt[arr[i]] == maxCnt && arr[i] < minMode)) {
                    maxCnt = cnt[arr[i]];
                    minMode = arr[i];
                }
            }
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                cnt[arr[i]]++;
                
                // 更新众数
                if (cnt[arr[i]] > maxCnt || (cnt[arr[i]] == maxCnt && arr[i] < minMode)) {
                    maxCnt = cnt[arr[i]];
                    minMode = arr[i];
                }
            }
            
            // 结合预处理结果
            if (belongL + 1 <= belongR - 1) {
                int preMode = f[belongL + 1][belongR - 1];
                int preCnt = 0;
                
                // 计算预处理众数在当前区间中的出现次数
                for (int i = blockLeft[belongL + 1]; i <= blockRight[belongR - 1]; i++) {
                    if (arr[i] == preMode) preCnt++;
                }
                
                // 更新众数
                if (preCnt > maxCnt || (preCnt == maxCnt && preMode < minMode)) {
                    maxCnt = preCnt;
                    minMode = preMode;
                }
            }
        }
        
        return minMode;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        // 构建分块结构
        build(n);
        
        // 处理操作
        for (int i = 1; i <= n; i++) {
            String[] operation = reader.readLine().split(" ");
            int l = Integer.parseInt(operation[0]);
            int r = Integer.parseInt(operation[1]);
            
            // 查询区间[l, r]的最小众数
            out.println(query(l, r));
        }
        
        out.flush();
        out.close();
    }
}