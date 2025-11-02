package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * 序列 - Java实现
 * 
 * 题目来源：洛谷 P3863
 * 题目描述：
 * 给定一个长度为n的数组arr，初始时刻认为是第0秒
 * 接下来发生m条操作，第i条操作发生在第i秒，操作类型如下
 * 操作 1 l r v : arr[l..r]范围上每个数加v，v可能是负数
 * 操作 2 x v   : 不包括当前这一秒，查询过去多少秒内，arr[x] >= v
 * 
 * 数据范围：
 * 2 <= n、m <= 10^5
 * -10^9 <= 数组中的值 <= +10^9
 * 
 * 解题思路：
 * 这是一个时间轴上的分块问题。我们需要处理两种操作：
 * 1. 区间加法操作：对时间轴上的区间进行加法操作
 * 2. 查询操作：查询在某个时间点之前，满足条件的时间点数量
 * 
 * 关键思路是将所有事件离线处理，按位置排序后使用分块算法：
 * 1. 将所有修改和查询事件存储下来
 * 2. 按位置排序，相同位置时修改事件优先于查询事件
 * 3. 使用分块维护时间轴上的信息
 * 4. 对于每个位置，维护时间轴上该位置的值变化情况
 * 
 * 时间复杂度分析：
 * - 预处理（排序）：O((m+n) * log(m+n))
 * - 每次区间加法操作：O(√m)
 * - 每次查询操作：O(√m)
 * - 总体时间复杂度：O((m+n) * log(m+n) + (m+n) * √m)
 * 
 * 空间复杂度：O(m+n)
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 处理空区间情况
 *    - 处理边界条件
 * 2. 性能优化：
 *    - 使用分块算法优化区间操作
 *    - 离线处理减少重复计算
 * 3. 鲁棒性：
 *    - 处理大数值运算（使用long类型）
 *    - 保证在各种数据分布下的稳定性能
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3863
 */

class Event {
    int op, x, t, v, q;
    
    public Event(int op, int x, int t, int v, int q) {
        this.op = op;
        this.x = x;
        this.t = t;
        this.v = v;
        this.q = q;
    }
}

public class Sequence_Java {
    public static final int MAXN = 100001;
    public static final int MAXB = 501;
    
    private int n, m;
    private int[] arr = new int[MAXN];
    
    // 事件数组，存储所有操作事件
    private Event[] events = new Event[MAXN << 2];
    private int eventCount = 0; // 事件计数器
    private int queryCount = 0; // 查询计数器
    
    // tim[i] = v，表示在i号时间点，所有数字都增加v
    private long[] tim = new long[MAXN];
    // 时间块内的所有值要排序，方便查询 >= v的数字个数
    private long[] sortv = new long[MAXN];
    
    // 时间分块相关变量
    private int blockSize, blockNum; // 块大小和块数量
    private int[] blockIndex = new int[MAXN]; // 每个时间点所属的块
    private int[] blockLeft = new int[MAXB]; // 每个块的左边界
    private int[] blockRight = new int[MAXB]; // 每个块的右边界
    private long[] lazy = new long[MAXB]; // 每个块的懒惰标记
    
    // 每个查询的答案
    private int[] ans = new int[MAXN];
    
    /**
     * 初始化分块结构
     * 
     * @param size 时间轴大小
     */
    public void init(int size) {
        this.m = size;
        // 设置块大小为sqrt(m)
        this.blockSize = (int) Math.sqrt(m);
        // 计算块数量
        this.blockNum = (m + blockSize - 1) / blockSize;
        
        // 初始化每个时间点所属的块
        for (int i = 1; i <= m; i++) {
            blockIndex[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化每个块的边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, m);
        }
        
        // 初始化懒惰标记
        Arrays.fill(lazy, 0);
    }
    
    /**
     * 对指定时间区间进行加法操作并维护排序数组
     * 
     * @param l 时间区间左端点
     * @param r 时间区间右端点
     * @param v 要增加的值
     */
    private void innerAdd(int l, int r, long v) {
        // 对时间区间内每个时间点加上v
        for (int i = l; i <= r; i++) {
            tim[i] += v;
        }
        // 更新该块的排序数组
        int blockId = blockIndex[l];
        for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
            sortv[i] = tim[i];
        }
        // 对块内时间点重新排序
        long[] temp = new long[blockRight[blockId] - blockLeft[blockId] + 1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = sortv[blockLeft[blockId] + i];
        }
        Arrays.sort(temp);
        for (int i = 0; i < temp.length; i++) {
            sortv[blockLeft[blockId] + i] = temp[i];
        }
    }
    
    /**
     * 时间区间加法操作
     * 
     * @param l 时间区间左端点
     * @param r 时间区间右端点
     * @param v 要增加的值
     */
    public void add(int l, int r, long v) {
        // 处理空区间
        if (l > r) {
            return;
        }
        
        int leftBlock = blockIndex[l];
        int rightBlock = blockIndex[r];
        
        // 如果区间在同一个块内
        if (leftBlock == rightBlock) {
            innerAdd(l, r, v);
        } else {
            // 处理左边不完整块
            innerAdd(l, blockRight[leftBlock], v);
            // 处理右边不完整块
            innerAdd(blockLeft[rightBlock], r, v);
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                lazy[i] += v;
            }
        }
    }
    
    /**
     * 在指定时间区间内查询大于等于v的数字个数（暴力方法）
     * 
     * @param l 时间区间左端点
     * @param r 时间区间右端点
     * @param v 比较值
     * @return 大于等于v的数字个数
     */
    private int innerQuery(int l, int r, long v) {
        v -= lazy[blockIndex[l]]; // 考虑块的懒惰标记
        int count = 0;
        for (int i = l; i <= r; i++) {
            if (tim[i] >= v) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 第i块内>= v的数字个数（使用二分查找）
     * 
     * @param blockId 块编号
     * @param v 比较值
     * @return 第i块内>= v的数字个数
     */
    private int blockCount(int blockId, long v) {
        v -= lazy[blockId]; // 考虑块的懒惰标记
        int left = blockLeft[blockId];
        int right = blockRight[blockId];
        int pos = blockRight[blockId] + 1;
        
        // 二分查找第一个大于等于v的位置
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (sortv[mid] >= v) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return blockRight[blockId] - pos + 1;
    }
    
    /**
     * 查询时间区间内大于等于v的数字个数
     * 
     * @param l 时间区间左端点
     * @param r 时间区间右端点
     * @param v 比较值
     * @return 大于等于v的数字个数
     */
    public int query(int l, int r, long v) {
        // 处理空区间
        if (l > r) {
            return 0;
        }
        
        int leftBlock = blockIndex[l];
        int rightBlock = blockIndex[r];
        int count = 0;
        
        // 如果区间在同一个块内
        if (leftBlock == rightBlock) {
            count += innerQuery(l, r, v);
        } else {
            // 处理左边不完整块
            count += innerQuery(l, blockRight[leftBlock], v);
            // 处理右边不完整块
            count += innerQuery(blockLeft[rightBlock], r, v);
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                count += blockCount(i, v);
            }
        }
        return count;
    }
    
    /**
     * 添加修改事件
     * 
     * @param x 位置
     * @param t 时间
     * @param v 修改值
     */
    public void addChange(int x, int t, int v) {
        events[++eventCount] = new Event(1, x, t, v, 0);
    }
    
    /**
     * 添加查询事件
     * 
     * @param x 位置
     * @param t 时间
     * @param v 查询标准
     */
    public void addQuery(int x, int t, int v) {
        events[++eventCount] = new Event(2, x, t, v, ++queryCount);
    }
    
    /**
     * 初始化分块结构和事件排序
     */
    public void prepare() {
        // 按位置排序，位置相同的按时间排序
        Arrays.sort(events, 1, eventCount + 1, (a, b) -> {
            if (a.x != b.x) {
                return a.x - b.x;
            }
            return a.t - b.t;
        });
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] nm = reader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        
        Sequence_Java solution = new Sequence_Java();
        solution.n = n;
        
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            solution.arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        m++; // 时间轴重新定义，1是初始时刻、2、3 ... m+1
        solution.init(m);
        
        // 读取所有操作
        for (int t = 2; t <= m; t++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            if (op == 1) {
                int l = Integer.parseInt(operation[1]);
                int r = Integer.parseInt(operation[2]);
                int v = Integer.parseInt(operation[3]);
                // 使用差分数组技巧处理区间加法
                solution.addChange(l, t, v);
                solution.addChange(r + 1, t, -v);
            } else {
                int x = Integer.parseInt(operation[1]);
                int v = Integer.parseInt(operation[2]);
                solution.addQuery(x, t, v);
            }
        }
        
        solution.prepare();
        
        // 处理所有事件
        for (int i = 1; i <= solution.eventCount; i++) {
            Event event = solution.events[i];
            if (event.op == 1) {
                // 处理修改事件
                solution.add(event.t, m, event.v);
            } else {
                // 处理查询事件
                solution.ans[event.q] = solution.query(1, event.t - 1, (long) event.v - solution.arr[event.x]);
            }
        }
        
        // 输出所有查询结果
        for (int i = 1; i <= solution.queryCount; i++) {
            writer.println(solution.ans[i]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}