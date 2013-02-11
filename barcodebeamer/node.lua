gl.setup(1024, 768)

font = resource.load_font("silkscreen.ttf")

space = 40;

-- store the last position of the user list
displayUserLastPos = 0;

displayOrderLastPos = 0;

userListIndex = 0;
userList = {}

orderListIndex = 0;
orderList = {}

orderPrice = 0.0;

--lastAction = sys.now();


util.data_mapper{
    ["scanner/setPrice"] = function(price)
        orderPrice = price
    end;
    ["scanner/orderClear"] = function()
        orderList = {}
        orderListIndex = 0;
        displayOrderLastPos = 0;
    end;
    ["scanner/orderAdd"] = function(name)
	orderListIndex = orderListIndex + 1
        orderList[orderListIndex] = name
    end;
    ["scanner/userClear"] = function()
        userList = {}
        userListIndex = 0;
        displayUserLastPos = 0;
    end;
    ["scanner/userAdd"] = function(name)
        userListIndex = userListIndex + 1 
        userList[userListIndex] = name
    end;
    ["scanner/userUndo"] = function()
        table.remove(userList, userListIndex)
        userListIndex = userListIndex -1
        displayUserLastPos = displayUserLastPos - space
    end;
    ["scanner/orderUndo"] = function()
        table.remove(orderList, orderListIndex)
        orderListIndex = orderListIndex -1
        displayOrderLastPos = displayOrderLastPos - space
    end;
}


function node.render()

    -- wirte the user list
    font:write(20, 20, "User", 40, 1,0,0,1)

    for i,name in ipairs(userList) do 
	font:write(20, 50+(space*i), name, 40, 1,1,1,1)
        displayUserLastPos = 50+(space*i)
    end

    -- write the order list
    font:write(500, 20, "Items", 40, 1,0,0,1)

    for i,name in ipairs(orderList) do
        font:write(500, 50+(space*i), name, 40, 1,1,1,1)
        displayOrderLastPos = 50+(space*i)
    end


    -- write order price
    maxDist = displayUserLastPos
    if displayUserLastPos < displayOrderLastPos then
        maxDist = displayOrderLastPos
    end
    font:write(20, 150 + maxDist, "Price each: " .. orderPrice .. " Euro", 40, 1,1,1,1)


    --print(sys.now());

end
