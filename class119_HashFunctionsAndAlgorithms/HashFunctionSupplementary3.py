    @staticmethod
    def four_sum_ii(nums1: List[int], nums2: List[int], nums3: List[int], nums4: List[int]) -> int:
        """
        LeetCode 454. 4Sum II (四数相加 II)
        题目来源: https://leetcode.com/problems/4sum-ii/
        
        题目描述:
        给定四个包含整数的数组列表 A, B, C, D, 计算有多少个元组 (i, j, k, l) ，使得 A[i] + B[j] + C[k] + D[l] = 0。
        为了使问题简单化，所有的 A, B, C, D 具有相同的长度 N，且 0 ≤ N ≤ 500。所有整数的范围在 -2^28 到 2^28 - 1 之间，
        最终结果不会超过 2^31 - 1。
        
        示例:
        输入:
        A = [ 1, 2]
        B = [-2,-1]
        C = [-1, 2]
        D = [ 0, 2]
        输出: 2
        解释:
        两个元组如下:
        1. (0, 0, 0, 1) -> A[0] + B[0] + C[0] + D[1] = 1 + (-2) + (-1) + 2 = 0
        2. (1, 1, 0, 0) -> A[1] + B[1] + C[0] + D[0] = 2 + (-1) + (-1) + 0 = 0
        
        算法思路:
        1. 将四个数组分成两组，分别计算两数之和
        2. 使用哈希表存储第一组中所有可能的两数之和及其出现次数
        3. 遍历第二组中的所有两数之和，检查其相反数是否在哈希表中
        
        时间复杂度: O(n^2)，其中n是数组长度
        空间复杂度: O(n^2)
        """
        # 计算nums1和nums2中所有可能的两数之和及其出现次数
        sum_count = {}
        for a in nums1:
            for b in nums2:
                sum_ab = a + b
                sum_count[sum_ab] = sum_count.get(sum_ab, 0) + 1
        
        # 计算有多少对nums3和nums4的元素之和等于-(nums1+nums2)
        count = 0
        for c in nums3:
            for d in nums4:
                sum_cd = -(c + d)
                if sum_cd in sum_count:
                    count += sum_count[sum_cd]
        
        return count

    @staticmethod
    def contiguous_array(nums: List[int]) -> int:
        """
        LeetCode 525. Contiguous Array (连续数组)
        题目来源: https://leetcode.com/problems/contiguous-array/
        
        题目描述:
        给定一个二进制数组 nums, 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
        
        示例:
        输入: [0,1]
        输出: 2
        说明: [0, 1] 是具有相同数量0和1的最长连续子数组。
        
        输入: [0,1,0]
        输出: 2
        说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。
        
        算法思路:
        1. 将0视为-1，1保持不变，问题转化为求和为0的最长子数组
        2. 使用前缀和和哈希表记录每个前缀和第一次出现的位置
        3. 当遇到相同的前缀和时，计算子数组长度
        
        时间复杂度: O(n)，其中n是数组长度
        空间复杂度: O(n)
        """
        # 初始化哈希表，前缀和为0的位置为-1
        prefix_sum_index = {0: -1}
        prefix_sum = 0
        max_length = 0
        
        for i, num in enumerate(nums):
            # 将0视为-1，1保持不变
            prefix_sum += 1 if num == 1 else -1
            
            # 如果当前前缀和之前出现过，计算子数组长度
            if prefix_sum in prefix_sum_index:
                max_length = max(max_length, i - prefix_sum_index[prefix_sum])
            else:
                # 记录前缀和第一次出现的位置
                prefix_sum_index[prefix_sum] = i
        
        return max_length

    @staticmethod
    def line_reflection(points: List[List[int]]) -> bool:
        """
        LeetCode 356. Line Reflection (直线镜像)
        题目来源: https://leetcode.com/problems/line-reflection/
        
        题目描述:
        在一个二维平面上，如果我们放置一些点，那么可能会有一些点在同一条垂直线上。
        对于这样的每一条垂直线，我们可以找到其上最上面和最下面的点。
        如果这些点可以构成一个镜像，则返回 true；否则，返回 false。
        
        示例:
        输入: [[1,1],[-1,1]]
        输出: true
        
        输入: [[1,1],[-1,-1]]
        输出: false
        
        算法思路:
        1. 找到所有点的x坐标的最小值和最大值
        2. 计算对称轴的x坐标：(min_x + max_x) / 2
        3. 对于每个点，检查其关于对称轴的镜像点是否存在
        
        时间复杂度: O(n)，其中n是点的数量
        空间复杂度: O(n)
        """
        if not points:
            return True
        
        # 去除重复点
        point_set = set()
        for x, y in points:
            point_set.add((x, y))
        
        # 找到所有点的x坐标的最小值和最大值
        min_x = float('inf')
        max_x = float('-inf')
        for x, y in point_set:
            min_x = min(min_x, x)
            max_x = max(max_x, x)
        
        # 计算对称轴的x坐标
        mid_x = min_x + max_x
        
        # 检查每个点的镜像是否存在
        for x, y in point_set:
            mirror_x = mid_x - x
            if (mirror_x, y) not in point_set:
                return False
        
        return True

    @staticmethod
    def find_duplicate_subtrees(root):
        """
        LeetCode 652. Find Duplicate Subtrees (寻找重复的子树)
        题目来源: https://leetcode.com/problems/find-duplicate-subtrees/
        
        题目描述:
        给定一棵二叉树，返回所有重复的子树。对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可。
        两棵树重复是指它们具有相同的结构以及相同的结点值。
        
        示例:
        输入:
            1
           / \\
          2   3
         /   / \\
        4   2   4
           /
          4
        输出: [2,4]
        解释: 上面的2和4都是重复的子树。
        
        算法思路:
        1. 使用哈希表存储每个子树的序列化表示及其出现次数
        2. 使用后序遍历序列化每个子树
        3. 当某个子树的序列化表示出现第二次时，将其添加到结果中
        
        时间复杂度: O(n^2)，其中n是树中节点数量，序列化每个子树需要O(n)时间
        空间复杂度: O(n^2)
        """
        # 定义TreeNode类
        class TreeNode:
            def __init__(self, val=0, left=None, right=None):
                self.val = val
                self.left = left
                self.right = right
        
        def serialize(node, subtree_map, result):
            if not node:
                return "#"
            
            # 后序遍历序列化子树
            left_serialize = serialize(node.left, subtree_map, result)
            right_serialize = serialize(node.right, subtree_map, result)
            
            # 当前子树的序列化表示
            serialized = left_serialize + "," + right_serialize + "," + str(node.val)
            
            # 更新子树出现次数
            subtree_map[serialized] = subtree_map.get(serialized, 0) + 1
            
            # 如果当前子树出现第二次，添加到结果中
            if subtree_map[serialized] == 2:
                result.append(node)
            
            return serialized
        
        result = []
        subtree_map = {}
        serialize(root, subtree_map, result)
        return result

    @staticmethod
    def unique_word_abbreviation(dictionary: List[str], word: str) -> bool:
        """
        LeetCode 288. Unique Word Abbreviation (单词的唯一缩写)
        题目来源: https://leetcode.com/problems/unique-word-abbreviation/
        
        题目描述:
        一个单词的缩写需要遵循 <首字母><中间字母数><尾字母> 的格式。
        例如，"abcde" 可以缩写为 "a3e"。
        给定一个字典和一个单词，判断该单词的缩写在字典中是否唯一。
        若单词的缩写在字典中没有任何 其他 单词与其缩写相同，则被认为是唯一的。
        
        示例:
        输入: dictionary = ["deer", "door", "cake", "card"], word = "dear"
        输出: false
        解释: 单词 "dear" 的缩写是 "d2r"，在字典中与 "deer" 的缩写相同。
        
        算法思路:
        1. 定义一个函数计算单词的缩写
        2. 使用哈希表存储字典中每个缩写对应的单词集合
        3. 检查word的缩写在字典中是否唯一
        
        时间复杂度: O(n)，其中n是字典中单词的数量
        空间复杂度: O(n)
        """
        def get_abbreviation(word):
            if len(word) <= 2:
                return word
            return word[0] + str(len(word) - 2) + word[-1]
        
        # 构建缩写到单词集合的映射
        abbr_to_words = {}
        for dict_word in dictionary:
            abbr = get_abbreviation(dict_word)
            if abbr not in abbr_to_words:
                abbr_to_words[abbr] = set()
            abbr_to_words[abbr].add(dict_word)
        
        # 计算word的缩写
        word_abbr = get_abbreviation(word)
        
        # 检查word的缩写是否唯一
        if word_abbr not in abbr_to_words:
            return True
        
        # 如果缩写对应的单词集合中只有word自己，也算唯一
        if len(abbr_to_words[word_abbr]) == 1 and word in abbr_to_words[word_abbr]:
            return True
        
        return False

    @staticmethod
    def logger_rate_limiter():
        """
        LeetCode 359. Logger Rate Limiter (日志速率限制器)
        题目来源: https://leetcode.com/problems/logger-rate-limiter/
        
        题目描述:
        设计一个日志系统，可以流式接收日志消息。每条消息都有一个唯一的ID和时间戳。
        消息的时间戳是一个整数，表示消息发生的时间（以秒为单位）。
        实现一个日志速率限制器，每条独特的消息最多每10秒打印一次。
        
        示例:
        logger.shouldPrintMessage(1, "foo");  // 返回 true，打印消息
        logger.shouldPrintMessage(2, "bar");  // 返回 true，打印消息
        logger.shouldPrintMessage(3, "foo");  // 返回 false，不打印消息，因为距离上次打印 "foo" 不足10秒
        logger.shouldPrintMessage(8, "bar");  // 返回 false，不打印消息，因为距离上次打印 "bar" 不足10秒
        logger.shouldPrintMessage(10, "foo"); // 返回 false，不打印消息，因为距离上次打印 "foo" 不足10秒
        logger.shouldPrintMessage(11, "foo"); // 返回 true，打印消息，因为距离上次打印 "foo" 已经10秒了
        
        算法思路:
        1. 使用哈希表存储每个消息最后打印的时间戳
        2. 对于每个新消息，检查是否距离上次打印已经过了10秒
        
        时间复杂度: O(1) 每次操作
        空间复杂度: O(m)，其中m是不同消息的数量
        """
        class Logger:
            def __init__(self):
                # 存储消息最后打印的时间戳
                self.message_timestamps = {}
            
            def shouldPrintMessage(self, timestamp: int, message: str) -> bool:
                # 检查消息是否应该被打印
                if message not in self.message_timestamps or timestamp - self.message_timestamps[message] >= 10:
                    self.message_timestamps[message] = timestamp
                    return True
                return False
        
        # 使用示例
        logger = Logger()
        print(logger.shouldPrintMessage(1, "foo"))  # 返回 true
        print(logger.shouldPrintMessage(2, "bar"))  # 返回 true
        print(logger.shouldPrintMessage(3, "foo"))  # 返回 false
        print(logger.shouldPrintMessage(8, "bar"))  # 返回 false
        print(logger.shouldPrintMessage(10, "foo"))  # 返回 false
        print(logger.shouldPrintMessage(11, "foo"))  # 返回 true
        
        return logger

    @staticmethod
    def bulls_and_cows(secret: str, guess: str) -> str:
        """
        LeetCode 299. Bulls and Cows (猜数字游戏)
        题目来源: https://leetcode.com/problems/bulls-and-cows/
        
        题目描述:
        你在和朋友玩猜数字（Bulls and Cows）游戏，该游戏规则如下：
        1. 你写出一个秘密数字，并请朋友猜这个数字是多少。
        2. 朋友每猜测一次，你就会给他一个提示，告诉他的猜测数字中有多少位属于数字和确切位置都猜对了（称为"Bulls", 公牛），
           有多少位属于数字猜对了但是位置不对（称为"Cows", 奶牛）。
        3. 朋友根据提示继续猜，直到猜出秘密数字。
        
        请写出一个根据秘密数字和朋友的猜测数返回提示的函数，返回字符串的格式为 xAyB，x 表示公牛个数，y 表示奶牛个数。
        
        示例:
        输入: secret = "1807", guess = "7810"
        输出: "1A3B"
        解释: 数字和位置都对的是 8，数字对但位置不对的是 1, 7, 0
        
        算法思路:
        1. 遍历secret和guess，统计Bulls（位置和数字都正确的）
        2. 使用哈希表统计secret和guess中每个数字出现的次数
        3. 计算Cows（数字正确但位置不对的）= min(secret中数字出现次数, guess中数字出现次数) - Bulls
        
        时间复杂度: O(n)，其中n是字符串长度
        空间复杂度: O(1)，因为数字只有0-9，哈希表大小是常数
        """
        bulls = 0
        cows = 0
        
        # 统计secret和guess中每个数字出现的次数
        secret_count = [0] * 10
        guess_count = [0] * 10
        
        # 计算Bulls并统计数字出现次数
        for i in range(len(secret)):
            s = int(secret[i])
            g = int(guess[i])
            
            if s == g:
                bulls += 1
            else:
                secret_count[s] += 1
                guess_count[g] += 1
        
        # 计算Cows
        for i in range(10):
            cows += min(secret_count[i], guess_count[i])
        
        return f"{bulls}A{cows}B"

    @staticmethod
    def sort_characters_by_frequency(s: str) -> str:
        """
        LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序)
        题目来源: https://leetcode.com/problems/sort-characters-by-frequency/
        
        题目描述:
        给定一个字符串，请将字符串里的字符按照出现的频率降序排列。
        
        示例:
        输入: "tree"
        输出: "eert"
        解释: 'e'出现两次，'r'和't'都只出现一次。因此'e'必须出现在'r'和't'之前。此外，"eetr"也是一个有效的答案。
        
        算法思路:
        1. 使用哈希表统计每个字符出现的频率
        2. 按照频率降序排列字符
        3. 根据排序后的结果重建字符串
        
        时间复杂度: O(n log k)，其中n是字符串长度，k是不同字符的数量
        空间复杂度: O(n)
        """
        # 统计字符频率
        char_count = {}
        for char in s:
            char_count[char] = char_count.get(char, 0) + 1
        
        # 按频率降序排列字符
        sorted_chars = sorted(char_count.keys(), key=lambda x: char_count[x], reverse=True)
        
        # 重建字符串
        result = []
        for char in sorted_chars:
            result.append(char * char_count[char])
        
        return ''.join(result)

    @staticmethod
    def longest_substring_with_at_most_two_distinct(s: str) -> int:
        """
        LeetCode 159. Longest Substring with At Most Two Distinct Characters (至多包含两个不同字符的最长子串)
        题目来源: https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/
        
        题目描述:
        给定一个字符串 s，找出至多包含两个不同字符的最长子串的长度。
        
        示例:
        输入: "eceba"
        输出: 3
        解释: 子串 "ece" 包含2个不同的字符。
        
        输入: "ccaabbb"
        输出: 5
        解释: 子串 "aabbb" 包含2个不同的字符。
        
        算法思路:
        1. 使用滑动窗口和哈希表
        2. 维护一个窗口，使其最多包含两个不同字符
        3. 当窗口中不同字符超过两个时，移动左指针
        
        时间复杂度: O(n)，其中n是字符串长度
        空间复杂度: O(1)，因为最多只有两个不同字符
        """
        if not s:
            return 0
        
        # 滑动窗口左右指针
        left = 0
        max_length = 0
        char_count = {}
        
        for right in range(len(s)):
            # 更新字符计数
            char_count[s[right]] = char_count.get(s[right], 0) + 1
            
            # 当窗口中不同字符超过两个时，移动左指针
            while len(char_count) > 2:
                char_count[s[left]] -= 1
                if char_count[s[left]] == 0:
                    del char_count[s[left]]
                left += 1
            
            # 更新最长子串长度
            max_length = max(max_length, right - left + 1)
        
        return max_length

    @staticmethod
    def encode_and_decode_tiny_url():
        """
        LeetCode 535. Encode and Decode TinyURL (TinyURL 的加密与解密)
        题目来源: https://leetcode.com/problems/encode-and-decode-tinyurl/
        
        题目描述:
        TinyURL是一种URL简化服务，比如：当你输入一个URL https://leetcode.com/problems/design-tinyurl 时，
        它将返回一个简化的URL http://tinyurl.com/4e9iAk.
        
        要求：设计一个 TinyURL 的加密 encode 和解密 decode 的方法。
        你的加密和解密算法如何设计和运作是没有限制的，你只需要保证一个URL可以被加密成一个TinyURL，
        并且这个TinyURL可以用解密方法恢复成原本的URL。
        
        算法思路:
        1. 使用哈希表存储长URL到短URL的映射，以及短URL到长URL的映射
        2. 生成短URL时，可以使用递增ID、随机字符串或哈希函数
        3. 这里使用简单的递增ID方法
        
        时间复杂度: O(1) 每次操作
        空间复杂度: O(n)，其中n是不同URL的数量
        """
        class Codec:
            def __init__(self):
                self.url_to_code = {}
                self.code_to_url = {}
                self.base = "http://tinyurl.com/"
                self.counter = 0
            
            def encode(self, longUrl: str) -> str:
                # 如果长URL已经被编码过，直接返回对应的短URL
                if longUrl in self.url_to_code:
                    return self.base + self.url_to_code[longUrl]
                
                # 生成新的短URL编码
                self.counter += 1
                code = str(self.counter)
                
                # 存储映射关系
                self.url_to_code[longUrl] = code
                self.code_to_url[code] = longUrl
                
                return self.base + code
            
            def decode(self, shortUrl: str) -> str:
                # 提取短URL中的编码部分
                code = shortUrl.replace(self.base, "")
                
                # 返回对应的长URL
                return self.code_to_url.get(code, "")
        
        # 使用示例
        codec = Codec()
        url = "https://leetcode.com/problems/design-tinyurl"
        tiny_url = codec.encode(url)
        original_url = codec.decode(tiny_url)
        print(f"Original URL: {url}")
        print(f"Tiny URL: {tiny_url}")
        print(f"Decoded URL: {original_url}")
        
        return codec