import request from "@/utils/request";

const dictsText = 'dictsText';

export async function saveAlldics()  {
  await request({
    url: "/account/dict/findAll",
    method: "get",
  }).then(res => {localStorage.setItem(dictsText, JSON.stringify(res));
    debugger
    const r = getDicByValue('ORG_TYPE');
    
  });
}

export function getDicByValue(value){
    debugger
    const root = JSON.parse(localStorage.getItem(dictsText));
    const targetSet = new Set(root.children);
    const r = targetSet.filter(child => child.dictValue === value);
    return r;
}
