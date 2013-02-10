gl.setup(1024, 768)

font = resource.load_font("silkscreen.ttf")

space = 40;

-- store the last position of the user list
displayUserLastPos = 0;

displayOrderLastPos = 0;

userList = {}
userList[1] = "Daniel"
userList[2] = "William"


orderList = {}
orderList[1] = "Urtrunk"
orderList[2] = "Wodan"
orderList[3] = "Fritz Cola"
orderList[4] = "Zapfle"
orderList[5] = "Zapfle - Radler"

orderPrice = 2.95;


function node.render()

    -- wirte the user list
    font:write(20, 20, "User", 40, 1,1,1,1)

    for i,name in ipairs(userList) do 
	font:write(20, 50+(space*i), name, 40, 1,1,1,1)
        displayUserLastPos = 50+(space*i)
    end

    -- write the order list
    font:write(500, 20, "Items", 40, 1,1,1,1)

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

end
