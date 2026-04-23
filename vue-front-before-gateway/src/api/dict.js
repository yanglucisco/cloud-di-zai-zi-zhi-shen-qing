import request from "@/utils/request";
import { startOneTimeTimer } from "@/utils/timer";

const dictsText = "dictsText";

export async function saveAlldics() {
  const data = await request({
    url: "/account/dict/findAll",
    method: "get",
  });
  localStorage.setItem(dictsText, JSON.stringify(data));
  startOneTimeTimer(30000, async () => {
    const data = await request({
      url: "/account/dict/findAll",
      method: "get",
    });
    localStorage.setItem(dictsText, JSON.stringify(data));
  });
}

export function getOrgTypesDic() {
  return getDicByValue("ORG_TYPE");
}

function getDicByValue(value) {
  debugger;
  const root = JSON.parse(localStorage.getItem(dictsText));
  const r = root.children
    .filter((child) => child.dictValue === value)[0]
    .children.map((item) => ({
      label: item.dictLabel,
      value: item.dictValue,
    }));
  return r;
}
