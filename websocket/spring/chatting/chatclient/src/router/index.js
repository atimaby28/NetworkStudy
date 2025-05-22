
import { createRouter, createWebHashHistory } from "vue-router"
import MemberCreate from "@/views/MemberCreate.vue"
import LoginPage from "@/views/LoginPage.vue"
import MemberList from "@/views/MemberList.vue"
import SimpleWebsocket from "@/views/SimpleWebSocket.vue"
import StompChatPage from "@/views/StompChatPage.vue"


const routes = [ 
    {
        path:'/member/create',
        name: 'MemberCreate',
        component: MemberCreate
    },
    {
        path:'/login',
        name: 'LoginPage',
        component: LoginPage
    },
    {
        path:'/member/list',
        name: 'MemberList',
        component: MemberList
    },
        {
        path:'/chatpage',
        name: 'StompChatPage',
        component: StompChatPage
    },
    {
        path:'/simple/chat',
        name: 'SimpleWebsocket',
        component: SimpleWebsocket
    },
    // {
    //     path:'/chatpage/:roomId',
    //     name: 'StompChatPage',
    //     component: StompChatPage
    // },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;
