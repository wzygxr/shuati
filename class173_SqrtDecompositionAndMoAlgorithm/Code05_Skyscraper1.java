package class175;

// 雅加达的摩天楼问题 - 分块算法优化BFS (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/P3645
// 题目来源: https://uoj.ac/problem/111
// 题目大意: 有n个大楼，编号0~n-1，有m个狗子，编号0~m-1
// 每只狗子有两个参数，idx表示狗子的初始大楼，jump表示狗子的跳跃能力
// 狗子在i位置，可以来到 i - jump 或 i + jump，向左向右自由跳跃，但不能越界
// 0号狗子有消息希望传给1号狗子，所有狗子都可帮忙，返回至少传送几次，无法送达打印-1
// 约束条件: 1 <= n、m <= 30000

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.BitSet;

public class Code05_Skyscraper1 {

    // BFS节点类，记录当前位置、跳跃能力和已用时间
    static class Node {
        int idx, jump, time;

        Node(int i, int j, int t) {
            idx = i;
            jump = j;
            time = t;
        }
    }

    // 定义最大数组长度
    public static int MAXN = 30001;
    
    // n: 大楼数量, m: 狗子数量
    public static int n, m;
    
    // 邻接表存储每个大楼拥有的狗子列表
    // head[i]: 大楼i的狗子链表头节点
    public static int[] head = new int[MAXN];
    
    // next[i]: 链表中第i个节点的下一个节点
    public static int[] next = new int[MAXN];
    
    // to[i]: 链表中第i个节点存储的跳跃能力
    public static int[] to = new int[MAXN];
    
    // cnt: 链表节点计数器
    public static int cnt;

    // bfs过程使用的队列
    public static ArrayDeque<Node> que = new ArrayDeque<>();
    
    // vis[idx]是个位图，可以表示vis[idx][jump]是否出现过
    // 用于避免重复访问相同状态（位置+跳跃能力）
    public static BitSet[] vis = new BitSet[MAXN];

    /**
     * 添加狗子到邻接表
     * @param idx 大楼编号
     * @param jump 跳跃能力
     */
    public static void add(int idx, int jump) {
        // 创建新节点
        next[++cnt] = head[idx];
        to[cnt] = jump;
        head[idx] = cnt;
    }

    /**
     * 触发大楼idx中的所有狗子
     * @param idx 大楼编号
     * @param time 当前时间
     */
    public static void trigger(int idx, int time) {
        // 遍历大楼idx中的所有狗子
        for (int e = head[idx], jump; e > 0; e = next[e]) {
            jump = to[e];
            // 如果这个状态（位置+跳跃能力）没有访问过
            if (!vis[idx].get(jump)) {
                // 标记为已访问
                vis[idx].set(jump);
                // 加入队列
                que.addLast(new Node(idx, jump, time));
            }
        }
        // 清空该大楼的狗子列表，避免重复处理
        head[idx] = 0;
    }

    /**
     * 扩展状态
     * @param idx 大楼编号
     * @param jump 跳跃能力
     * @param time 当前时间
     */
    public static void extend(int idx, int jump, int time) {
        // 触发该大楼的所有狗子
        trigger(idx, time);
        
        // 如果这个状态（位置+跳跃能力）没有访问过
        if (!vis[idx].get(jump)) {
            // 标记为已访问
            vis[idx].set(jump);
            // 加入队列
            que.addLast(new Node(idx, jump, time));
        }
    }

    /**
     * BFS搜索最短路径
     * @param s 起始大楼
     * @param t 目标大楼
     * @return 最少传送次数，无法送达返回-1
     */
    public static int bfs(int s, int t) {
        // 如果起始和目标相同，不需要传送
        if (s == t) {
            return 0;
        }
        
        // 初始化vis数组
        for (int i = 0; i < MAXN; i++) {
            vis[i] = new BitSet();
        }
        
        // 触发起始大楼的所有狗子
        trigger(s, 0);
        
        // BFS过程
        while (!que.isEmpty()) {
            // 取出队首节点
            Node cur = que.pollFirst();
            int idx = cur.idx;
            int jump = cur.jump;
            int time = cur.time;
            
            // 如果向左或向右跳跃能到达目标大楼
            if (idx - jump == t || idx + jump == t) {
                // 返回传送次数+1
                return time + 1;
            }
            
            // 向左跳跃
            if (idx - jump >= 0) {
                extend(idx - jump, jump, time + 1);
            }
            
            // 向右跳跃
            if (idx + jump < n) {
                extend(idx + jump, jump, time + 1);
            }
        }
        // 无法送达
        return -1;
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取大楼数量n和狗子数量m
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取起始狗子和目标狗子的信息
        int s = in.nextInt();
        int sjump = in.nextInt();
        int t = in.nextInt();
        int tjump = in.nextInt();
        
        // 添加起始和目标狗子
        add(s, sjump);
        add(t, tjump);
        
        // 读取其他狗子的信息
        for (int i = 2, idx, jump; i < m; i++) {
            idx = in.nextInt();
            jump = in.nextInt();
            add(idx, jump);
        }
        
        // BFS搜索最短路径
        out.println(bfs(s, t));
        out.flush();
        out.close();
    }

    // 高效读取工具类，用于加快输入输出速度
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
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