/**
 * 🌟 纯本地 SVG 头像生成器 🌟
 * 原理：根据传入的名字提取首字母，并利用名字的哈希值计算出一个固定的背景颜色。
 * 优势：不依赖任何外部网络，渲染极快，永不裂图！
 */
export const generateAvatar = (name) => {
  if (!name) name = 'U';

  // 1. 提取首个字符（汉字或字母），如果是字母则转大写
  const char = name.charAt(0).toUpperCase();

  // 2. 准备一套好看的莫兰迪色系背景色
  const colors = ['#3b82f6', '#10b981', '#8b5cf6', '#f59e0b', '#ef4444', '#ec4899', '#06b6d4'];

  // 3. 计算名字的哈希值，确保同一个名字永远生成同一种颜色
  let hash = 0;
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash);
  }
  const color = colors[Math.abs(hash) % colors.length];

  // 4. 拼装成 SVG 图像代码
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
      <rect width="100" height="100" fill="${color}" />
      <text x="50" y="50" font-family="Arial, sans-serif" font-size="40" font-weight="bold" fill="#ffffff" dominant-baseline="central" text-anchor="middle">${char}</text>
  </svg>`;

  // 5. 转换为 Data URI 格式，直接喂给 <img src="...">
  return `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`;
};