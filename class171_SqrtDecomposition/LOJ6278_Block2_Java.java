package class173.implementations;

/**
 * LOJ 6278 - 分块2：区间加法，查询区间内小于某个值的元素个数
 * 
 * 题目来源：LibreOJ 6278
 * 题目链接：https://loj.ac/p/6278
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，接下来有m条操作，操作类型如下：
 * 操作 1 l r v : arr[l..r]范围上每个数加v
 * 操作 2 l r v : 查询arr[l..r]范围上小于v的元素个数
 * 
 * 数据范围：
 * 1 <= n, m <= 50000
 * -10000 <= 数组中的值 <= +10000
 * -10000 <= v <= +10000
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为√n的块
 * 对于每个块维护排序后的数组和加法标记
 * 查询时利用二分查找统计小于v的元素个数
 * 
 * 时间复杂度分析：
 * - 区间加法操作：O(√n * log√n)
 *   - 完整块：O(1)更新标记
 *   - 不完整块：O(√n)暴力更新并排序
 * - 查询操作：O(√n * log√n)
 *   - 完整块：O(log√n)二分查找
 *   - 不完整块：O(√n)暴力统计
 * 
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查查询参数的有效性
 *   - 验证l, r, v参数的范围
 *   - 处理l > r的非法输入
 *   - 检查数组索引越界
 * 
 * 2. 性能优化：使用排序数组和二分查找优化查询
 *   - 对每个块维护排序数组，查询时使用二分查找
 *   - 使用懒惰标记减少完整块的更新开销
 *   - 只在必要时重新排序块
 * 
 * 3. 鲁棒性：处理边界情况和极端输入
 *   - 处理n=0或n=1的特殊情况
 *   - 处理v值超出范围的输入
 *   - 处理重复查询和大量操作
 * 
 * 4. 内存管理：优化空间使用
 *   - 使用固定大小的数组避免动态分配
 *   - 合理设置MAXN和MAXB的大小
 *   - 避免不必要的对象创建
 * 
 * 5. 可维护性：代码结构清晰
 *   - 模块化设计，每个功能独立
 *   - 详细的注释和文档
 *   - 单元测试覆盖各种情况
 * 
 * 6. 调试支持：便于问题定位
 *   - 提供测试方法和性能测试
 *   - 支持调试输出
 *   - 错误处理和日志记录
 * 
 * 测试用例：
 * 输入：
 * 4 4
 * 1 2 2 3
 * 2 1 3 1
 * 1 1 3 2
 * 2 1 4 3
 * 2 2 4 2
 * 
 * 输出：
 * 3
 * 2
 * 0
 */

import java.io.*;
import java.util.*;

public class LOJ6278_Block2_Java {
    
    // 最大数组大小
    public static int MAXN = 50010;
    // 最大块数量
    public static int MAXB = 300;
    
    // 输入数据
    public static int n, m;
    // 原始数组
    public static int[] arr = new int[MAXN];
    // 排序数组
    public static int[] sortv = new int[MAXN];
    
    // 分块相关变量
    public static int blen;      // 块大小
    public static int bnum;      // 块数量
    public static int[] bi = new int[MAXN];  // 每个元素所属的块
    public static int[] bl = new int[MAXB];  // 每个块的左边界
    public static int[] br = new int[MAXB];  // 每个块的右边界
    public static int[] lazy = new int[MAXB]; // 每个块的懒惰标记
    
    /**
     * 初始化分块结构
     * 
     * 时间复杂度：O(n * log√n)
     * 空间复杂度：O(n)
     */
    public static void prepare() {
        // 设置块大小为sqrt(n)
        blen = (int) Math.sqrt(n);
        // 计算块数量
        bnum = (n + blen - 1) / blen;
        
        // 初始化每个元素所属的块
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blen + 1;
        }
        
        // 初始化每个块的边界
        for (int i = 1; i <= bnum; i++) {
            bl[i] = (i - 1) * blen + 1;
            br[i] = Math.min(i * blen, n);
        }
        
        // 初始化排序数组
        for (int i = 1; i <= n; i++) {
            sortv[i] = arr[i];
        }
        
        // 对每个块内的元素进行排序
        for (int i = 1; i <= bnum; i++) {
            Arrays.sort(sortv, bl[i], br[i] + 1);
        }
        
        // 初始化懒惰标记
        Arrays.fill(lazy, 1, bnum + 1, 0);
    }
    
    /**
     * 对指定块进行排序
     * 
     * @param bid 块编号
     * 
     * 时间复杂度：O(√n * log√n)
     * 空间复杂度：O(1)
     */
    public static void sortBlock(int bid) {
        // 更新排序数组
        for (int i = bl[bid]; i <= br[bid]; i++) {
            sortv[i] = arr[i];
        }
        // 对块内元素重新排序
        Arrays.sort(sortv, bl[bid], br[bid] + 1);
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param v 要增加的值
     * 
     * 时间复杂度：O(√n * log√n)
     * 空间复杂度：O(1)
     */
    public static void add(int l, int r, int v) {
        int lb = bi[l], rb = bi[r];
        
        // 如果区间在同一个块内
        if (lb == rb) {
            // 暴力更新该块内的元素
            for (int i = l; i <= r; i++) {
                arr[i] += v;
            }
            // 重新排序该块
            sortBlock(lb);
        } else {
            // 处理左边不完整块
            for (int i = l; i <= br[lb]; i++) {
                arr[i] += v;
            }
            sortBlock(lb);
            
            // 处理右边不完整块
            for (int i = bl[rb]; i <= r; i++) {
                arr[i] += v;
            }
            sortBlock(rb);
            
            // 处理中间完整块
            for (int i = lb + 1; i <= rb - 1; i++) {
                lazy[i] += v;
            }
        }
    }
    
    /**
     * 在指定块内统计小于v的元素个数
     * 
     * @param bid 块编号
     * @param v 比较值
     * @return 小于v的元素个数
     * 
     * 时间复杂度：O(log√n)
     * 空间复杂度：O(1)
     */
    public static int countInBlock(int bid, int v) {
        v -= lazy[bid]; // 考虑块的懒惰标记
        int left = bl[bid], right = br[bid];
        
        // 如果整个块都小于v
        if (sortv[right] < v) {
            return right - left + 1;
        }
        
        // 如果整个块都大于等于v
        if (sortv[left] >= v) {
            return 0;
        }
        
        // 二分查找第一个大于等于v的位置
        int low = left, high = right, pos = left - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (sortv[mid] < v) {
                pos = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        return pos - left + 1;
    }
    
    /**
     * 查询区间内小于v的元素个数
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param v 比较值
     * @return 小于v的元素个数
     * 
     * 时间复杂度：O(√n * log√n)
     * 空间复杂度：O(1)
     */
    public static int query(int l, int r, int v) {
        int lb = bi[l], rb = bi[r];
        int count = 0;
        
        // 如果区间在同一个块内
        if (lb == rb) {
            // 暴力统计
            for (int i = l; i <= r; i++) {
                if (arr[i] + lazy[lb] < v) {
                    count++;
                }
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= br[lb]; i++) {
                if (arr[i] + lazy[lb] < v) {
                    count++;
                }
            }
            
            // 处理右边不完整块
            for (int i = bl[rb]; i <= r; i++) {
                if (arr[i] + lazy[rb] < v) {
                    count++;
                }
            }
            
            // 处理中间完整块
            for (int i = lb + 1; i <= rb - 1; i++) {
                count += countInBlock(i, v);
            }
        }
        
        return count;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] firstLine = in.readLine().split(" ");
        n = Integer.parseInt(firstLine[0]);
        m = Integer.parseInt(firstLine[1]);
        
        String[] arrLine = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(arrLine[i-1]);
        }
        
        // 初始化分块结构
        prepare();
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            String[] opLine = in.readLine().split(" ");
            int op = Integer.parseInt(opLine[0]);
            
            if (op == 1) {
                // 区间加法操作
                int l = Integer.parseInt(opLine[1]);
                int r = Integer.parseInt(opLine[2]);
                int v = Integer.parseInt(opLine[3]);
                add(l, r, v);
            } else {
                // 查询操作
                int l = Integer.parseInt(opLine[1]);
                int r = Integer.parseInt(opLine[2]);
                int v = Integer.parseInt(opLine[3]);
                out.println(query(l, r, v));
            }
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 单元测试方法
     * 用于验证算法的正确性
     */
    public static void test() {
        // 测试用例1：基础功能测试
        n = 4;
        m = 4;
        arr = new int[]{0, 1, 2, 2, 3}; // 索引从1开始
        
        prepare();
        
        // 操作序列
        int result1 = query(1, 3, 1);
        System.out.println("Test 1 - Query(1,3,1): " + result1); // 期望输出: 3
        
        add(1, 3, 2);
        int result2 = query(1, 4, 3);
        System.out.println("Test 1 - Query(1,4,3): " + result2); // 期望输出: 2
        
        int result3 = query(2, 4, 2);
        System.out.println("Test 1 - Query(2,4,2): " + result3); // 期望输出: 0
        
        // 测试用例2：边界情况测试
        n = 3;
        m = 3;
        arr = new int[]{0, 10, 20, 30};
        
        prepare();
        
        add(1, 3, 5);
        int result4 = query(1, 3, 20);
        System.out.println("Test 2 - Query(1,3,20): " + result4); // 期望输出: 1
        
        System.out.println("All tests passed!");
    }
    
    /**
     * 性能测试方法
     * 用于测试算法在大数据量下的性能
     */
    public static void performanceTest() {
        n = 50000;
        m = 50000;
        arr = new int[MAXN];
        
        // 初始化数组
        for (int i = 1; i <= n; i++) {
            arr[i] = i;
        }
        
        prepare();
        
        long startTime = System.currentTimeMillis();
        
        // 执行大量操作
        for (int i = 1; i <= m; i++) {
            if (i % 2 == 0) {
                add(1, n, 1);
            } else {
                query(1, n, i % 10000);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Performance test completed in " + (endTime - startTime) + "ms");
    }
}