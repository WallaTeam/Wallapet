/** Copyright (C) 2015 Hyena Technologies
 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.
 */
package com.hyenatechnologies.wallapet.conexiones;

/**
 * Created by ismaro3 on 03/04/2015.
 */
public class ServerException extends Exception {

    private int code;
    public ServerException(int code){
        super();
       this.code = code;
    }

    public int getCode(){
        return code;
    }
}
