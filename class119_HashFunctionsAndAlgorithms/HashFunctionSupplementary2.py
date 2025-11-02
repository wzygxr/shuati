    @staticmethod
    def max_points_on_line(points: List[List[int]]) -> int:
        """
        LeetCode 149. Max Points on a Line (直线上最多的点数)
        题目来源: https://leetcode.com/problems/max-points-on-a-line/
        
        题目描述:
        给你一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。
        求最多有多少个点在同一条直线上。
        
        示例:
        输入: points = [[1,1],[2,2],[3,3]]
        输出: 3
        
        算法思路:
        1. 对于每个点，计算它与其他点的斜率
        2. 使用哈希表统计相同斜率的点数
        3. 注意处理垂直线（斜率无穷大）和重复点的情况
        
        时间复杂度: O(n^2)，其中n是点的数量
        空间复杂度: O(n)
        """
        if not points:
            return 0
        if len(points) <= 2:
            return len(points)
        
        max_points = 0
        
        for i in range(len(points)):
            # 当前点
            x1, y1 = points[i]
            
            # 统计重复点和各斜率出现的次数
            slope_count = {}
            duplicates = 0
            
            for j in range(len(points)):
                if i == j:
                    continue
                
                x2, y2 = points[j]
                
                # 处理重复点
                if x1 == x2 and y1 == y2:
                    duplicates += 1
                    continue
                
                # 计算斜率
                if x1 == x2:  # 垂直线
                    slope = float('inf')
                else:
                    # 使用最大公约数简化分数，避免浮点数精度问题
                    dx = x2 - x1
                    dy = y2 - y1
                    gcd = math.gcd(dx, dy)
                    slope = (dy // gcd, dx // gcd)
                
                # 更新斜率计数
                slope_count[slope] = slope_count.get(slope, 0) + 1
            
            # 计算当前点能形成的最大直线点数
            current_max = duplicates + 1  # 包括当前点自身
            if slope_count:
                current_max = max(current_max, max(slope_count.values()) + 1)
            
            max_points = max(max_points, current_max)
        
        return max_points

    @staticmethod
    def contains_nearby_almost_duplicate(nums: List[int], k: int, t: int) -> bool:
        """
        LeetCode 220. Contains Duplicate III (存在重复元素 III)
        题目来源: https://leetcode.com/problems/contains-duplicate-iii/
        
        题目描述:
        给你一个整数数组 nums 和两个整数 k 与 t。
        请你判断是否存在两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t，同时又满足 abs(i - j) <= k。
        
        示例:
        输入：nums = [1,2,3,1], k = 3, t = 0
        输出：true
        
        算法思路:
        1. 使用桶排序的思想，将数字分到不同的桶中
        2. 如果两个数字的差值不超过t，则它们要么在同一个桶中，要么在相邻的桶中
        3. 维护一个大小为k的滑动窗口
        
        时间复杂度: O(n)，其中n是数组长度
        空间复杂度: O(min(n, k))
        """
        if t < 0:
            return False
        
        # 特殊情况处理
        if k <= 0:
            return False
        
        # 桶的大小为t+1，确保差值为t的两个数在同一个桶或相邻桶中
        bucket_size = t + 1
        buckets = {}
        
        for i, num in enumerate(nums):
            # 计算桶编号
            bucket_id = num // bucket_size
            
            # 检查当前桶
            if bucket_id in buckets:
                return True
            
            # 检查相邻的桶
            if bucket_id - 1 in buckets and abs(num - buckets[bucket_id - 1]) <= t:
                return True
            if bucket_id + 1 in buckets and abs(num - buckets[bucket_id + 1]) <= t:
                return True
            
            # 将当前数字放入桶中
            buckets[bucket_id] = num
            
            # 移除窗口外的桶
            if i >= k:
                del buckets[nums[i - k] // bucket_size]
        
        return False

    @staticmethod
    def find_all_numbers_disappeared(nums: List[int]) -> List[int]:
        """
        LeetCode 448. Find All Numbers Disappeared in an Array (找到所有数组中消失的数字)
        题目来源: https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
        
        题目描述:
        给你一个含 n 个整数的数组 nums，其中 nums[i] 在区间 [1, n] 内。
        请你找出所有在 [1, n] 范围内但没有出现在 nums 中的数字，并以数组的形式返回结果。
        
        示例:
        输入：nums = [4,3,2,7,8,2,3,1]
        输出：[5,6]
        
        算法思路:
        1. 利用数组索引作为哈希表
        2. 遍历数组，将每个数对应位置的数标记为负数
        3. 再次遍历数组，如果某个位置的数是正数，说明该位置对应的数没有出现
        
        时间复杂度: O(n)
        空间复杂度: O(1)，不使用额外空间
        """
        # 第一次遍历，标记出现的数字
        for num in nums:
            # 取绝对值，因为可能已经被标记为负数
            index = abs(num) - 1
            # 将对应位置的数标记为负数
            if nums[index] > 0:
                nums[index] = -nums[index]
        
        # 第二次遍历，找出未标记的位置
        result = []
        for i in range(len(nums)):
            if nums[i] > 0:
                result.append(i + 1)
        
        return result

    @staticmethod
    def first_unique_character(s: str) -> int:
        """
        LeetCode 387. First Unique Character in a String (字符串中的第一个唯一字符)
        题目来源: https://leetcode.com/problems/first-unique-character-in-a-string/
        
        题目描述:
        给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
        
        示例:
        输入: s = "leetcode"
        输出: 0
        
        输入: s = "loveleetcode"
        输出: 2
        
        算法思路:
        1. 使用哈希表统计每个字符出现的次数
        2. 再次遍历字符串，找到第一个出现次数为1的字符
        
        时间复杂度: O(n)，其中n是字符串长度
        空间复杂度: O(k)，其中k是字符集大小，最多为26
        """
        # 统计字符出现次数
        char_count = {}
        for char in s:
            char_count[char] = char_count.get(char, 0) + 1
        
        # 找到第一个出现次数为1的字符
        for i, char in enumerate(s):
            if char_count[char] == 1:
                return i
        
        return -1

    @staticmethod
    def longest_palindrome(s: str) -> int:
        """
        LeetCode 409. Longest Palindrome (最长回文串)
        题目来源: https://leetcode.com/problems/longest-palindrome/
        
        题目描述:
        给定一个包含大写字母和小写字母的字符串，找到通过这些字母构造成的最长的回文串。
        在构造过程中，请注意区分大小写。比如 "Aa" 不能当做一个回文字符串。
        
        示例:
        输入: "abccccdd"
        输出: 7
        解释: 我们可以构造的最长的回文串是"dccaccd", 它的长度是7。
        
        算法思路:
        1. 使用哈希表统计每个字符出现的次数
        2. 对于每个字符，可以使用其出现次数的最大偶数部分
        3. 如果存在出现奇数次的字符，可以额外使用一个字符作为回文中心
        
        时间复杂度: O(n)，其中n是字符串长度
        空间复杂度: O(k)，其中k是字符集大小
        """
        # 统计字符出现次数
        char_count = {}
        for char in s:
            char_count[char] = char_count.get(char, 0) + 1
        
        length = 0
        has_odd = False
        
        # 计算最长回文串长度
        for count in char_count.values():
            # 使用偶数部分
            length += count // 2 * 2
            # 检查是否有奇数次出现的字符
            if count % 2 == 1:
                has_odd = True
        
        # 如果存在奇数次出现的字符，可以额外使用一个字符作为回文中心
        if has_odd:
            length += 1
        
        return length

    @staticmethod
    def find_pairs_with_difference(nums: List[int], k: int) -> int:
        """
        LeetCode 532. K-diff Pairs in an Array (数组中的 k-diff 数对)
        题目来源: https://leetcode.com/problems/k-diff-pairs-in-an-array/
        
        题目描述:
        给定一个整数数组和一个整数 k，你需要在数组里找到不同的 k-diff 数对。
        这里将 k-diff 数对定义为一个整数对 (i, j)，其中 i 和 j 都是数组中的数字，且两数之差的绝对值是 k。
        
        示例:
        输入: [3, 1, 4, 1, 5], k = 2
        输出: 2
        解释: 数组中有两个 2-diff 数对, (1, 3) 和 (3, 5)。
        尽管数组中有两个1，但我们只应返回不同的数对的数量。
        
        算法思路:
        1. 使用哈希表存储数组中的数字及其出现次数
        2. 对于每个数字，检查其加上k或减去k的数字是否在哈希表中
        3. 特殊处理k=0的情况，此时需要找出数组中出现次数大于1的数字
        
        时间复杂度: O(n)，其中n是数组长度
        空间复杂度: O(n)
        """
        if k < 0:
            return 0
        
        # 统计数字出现次数
        num_count = {}
        for num in nums:
            num_count[num] = num_count.get(num, 0) + 1
        
        count = 0
        
        # 特殊处理k=0的情况
        if k == 0:
            for num, freq in num_count.items():
                if freq > 1:
                    count += 1
            return count
        
        # 处理k>0的情况
        for num in num_count:
            if num + k in num_count:
                count += 1
        
        return count

    @staticmethod
    def subarray_sum_divisible_by_k(nums: List[int], k: int) -> int:
        """
        LeetCode 974. Subarray Sums Divisible by K (和可被 K 整除的子数组)
        题目来源: https://leetcode.com/problems/subarray-sums-divisible-by-k/
        
        题目描述:
        给定一个整数数组 nums 和一个整数 k，返回其中元素之和可被 k 整除的（连续、非空）子数组的数目。
        
        示例:
        输入: nums = [4,5,0,-2,-3,1], k = 5
        输出: 7
        解释: 有7个子数组满足其元素之和可被 k = 5 整除：
        [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
        
        算法思路:
        1. 使用前缀和和哈希表
        2. 对于前缀和，我们关注的是前缀和除以k的余数
        3. 如果两个前缀和对k取余结果相同，则它们之间的子数组和能被k整除
        
        时间复杂度: O(n)，其中n是数组长度
        空间复杂度: O(k)，哈希表最多存储k个不同的余数
        """
        # 初始化哈希表，前缀和为0的情况出现1次
        prefix_sum_count = {0: 1}
        prefix_sum = 0
        count = 0
        
        for num in nums:
            # 计算前缀和
            prefix_sum = (prefix_sum + num) % k
            # 处理负数情况
            if prefix_sum < 0:
                prefix_sum += k
            
            # 如果当前前缀和的余数之前出现过，则可以形成能被k整除的子数组
            if prefix_sum in prefix_sum_count:
                count += prefix_sum_count[prefix_sum]
            
            # 更新前缀和余数的出现次数
            prefix_sum_count[prefix_sum] = prefix_sum_count.get(prefix_sum, 0) + 1
        
        return count

    @staticmethod
    def longest_word_in_dictionary(words: List[str]) -> str:
        """
        LeetCode 720. Longest Word in Dictionary (词典中最长的单词)
        题目来源: https://leetcode.com/problems/longest-word-in-dictionary/
        
        题目描述:
        给出一个字符串数组 words 组成的一本英语词典。从中找出最长的一个单词，该单词是由 words 词典中其他单词逐步添加一个字母组成。
        若其中有多个可行的答案，则返回答案中字典序最小的单词。若无答案，则返回空字符串。
        
        示例:
        输入: words = ["w","wo","wor","worl", "world"]
        输出: "world"
        解释: 单词"world"可由"w", "wo", "wor", 和 "worl"添加一个字母组成。
        
        算法思路:
        1. 将所有单词放入集合中，便于快速查找
        2. 对单词列表按长度降序、字典序升序排序
        3. 检查每个单词的所有前缀是否都在集合中
        
        时间复杂度: O(n * l^2)，其中n是单词数量，l是单词的平均长度
        空间复杂度: O(n * l)
        """
        # 将所有单词放入集合
        word_set = set(words)
        
        # 按长度降序、字典序升序排序
        sorted_words = sorted(words, key=lambda x: (-len(x), x))
        
        for word in sorted_words:
            valid = True
            # 检查所有前缀是否都在集合中
            for i in range(1, len(word)):
                if word[:i] not in word_set:
                    valid = False
                    break
            
            if valid:
                return word
        
        return ""

    @staticmethod
    def valid_sudoku(board: List[List[str]]) -> bool:
        """
        LeetCode 36. Valid Sudoku (有效的数独)
        题目来源: https://leetcode.com/problems/valid-sudoku/
        
        题目描述:
        判断一个 9x9 的数独是否有效。只需要根据以下规则，验证已经填入的数字是否有效即可。
        1. 数字 1-9 在每一行只能出现一次。
        2. 数字 1-9 在每一列只能出现一次。
        3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
        
        示例:
        输入:
        [
          ["5","3",".",".","7",".",".",".","."],
          ["6",".",".","1","9","5",".",".","."],
          [".","9","8",".",".",".",".","6","."],
          ["8",".",".",".","6",".",".",".","3"],
          ["4",".",".","8",".","3",".",".","1"],
          ["7",".",".",".","2",".",".",".","6"],
          [".","6",".",".",".",".","2","8","."],
          [".",".",".","4","1","9",".",".","5"],
          [".",".",".",".","8",".",".","7","9"]
        ]
        输出: true
        
        算法思路:
        1. 使用三个哈希表分别记录每行、每列、每个3x3宫格中数字的出现情况
        2. 遍历整个数独，检查是否有重复数字
        
        时间复杂度: O(1)，因为数独大小固定为9x9
        空间复杂度: O(1)
        """
        # 初始化哈希表
        rows = [set() for _ in range(9)]
        cols = [set() for _ in range(9)]
        boxes = [set() for _ in range(9)]
        
        # 遍历数独
        for i in range(9):
            for j in range(9):
                # 跳过空格
                if board[i][j] == '.':
                    continue
                
                num = board[i][j]
                # 计算3x3宫格的索引
                box_index = (i // 3) * 3 + j // 3
                
                # 检查是否有重复数字
                if num in rows[i] or num in cols[j] or num in boxes[box_index]:
                    return False
                
                # 记录数字出现情况
                rows[i].add(num)
                cols[j].add(num)
                boxes[box_index].add(num)
        
        return True