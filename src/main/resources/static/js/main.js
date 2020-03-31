function getIndex(list,id) {
    for(var i=0; i<list.length;i++){
        if (list[i].id === id){
            return i;
        }
    }
    return -1;
}



var messageApi = Vue.resource('/guide{/id}');

Vue.component('message-form',{
    props: ['messages','messageAttr'],
    data: function () {
        return {
            name: '',
            description: '',
            id:''
        }
    },
    watch:{
        messageAttr: function(newVal,oldVal){
            this.name = newVal.name;
            this.description = newVal.description;
            this.id = newVal.id;
        }
    },
template:
'<div>' +
    '<input type="text" placeholder="Название города" v-model = "name"/> ' +
    '<input type="text" placeholder="Описание" v-model = "description"/> ' +
    '<input type="button" value="Сохранить" @click="save"/> '+
    '</div>',
    methods: {
        save: function () {
            var message = {name: this.name,
            description: this.description};
            if (this.id){
                messageApi.update({id: this.id},message).then(result =>
            result.json().then(data => {
                var index = getIndex(this.messages,data.id);
                this.messages.splice(index,2,data);
                this.name = ''
                this.description = ''
                })
            )
            } else {
                messageApi.save({}, message).then(result =>
                result.json().then(data => {
                    this.messages.push(data);
                this.name = ''
                this.description = ''
            })
            )
            }
        }
    }
});

Vue.component('message-row',{
    props: ['message', 'editMethod','messages'],
    template: '<div>'+
    '<i>({{ message.name }})</i>{{ message.description }}'+
        '<span>'+
        '<input type="button" value="edit" @click="edit"/> '+
    '<input type="button" value="X" @click="del"/> '+
    '</span>'+
    '</div>',
    methods:{
        edit:function () {
            this.editMethod(this.message);
        },
        del:function () {
            messageApi.remove({id: this.message.id}).then(result =>{
                if(result.ok){
                    this.messages.splice(this.messages.indexOf(this.message),1)
            }
            })
        }
    }

});

Vue.component('messages-list', {
    props:['messages'],
    data: function () {
        return{
            message:null
        }
    },
    template:
    '<div>' +
    '<message-form :messages="messages" :messageAttr="message"/>' +
    '<message-row v-for="message in messages" :key="message.id" :message="message" :editMethod="editMethod" :messages="messages"/></div>',
    created: function () {
        messageApi.get().then(result =>
    result.json().then(data =>
           data.forEach(message => this.messages.push(message))
    )
    )
    },
    methods: {
        editMethod: function (message) {
            this.message = message;
            
        }
    }
});


var app = new Vue({
    el: '#app',
    template:'<messages-list :messages="messages" />',
    data: {
        messages: []
    }
});