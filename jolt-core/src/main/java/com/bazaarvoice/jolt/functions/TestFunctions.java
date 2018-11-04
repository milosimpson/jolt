/*
 * Copyright 2018 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bazaarvoice.jolt.functions;

public final class TestFunctions {

   @Function( "isTrue" )
   public boolean isTrueMember( @DefaultParam boolean b )
   {
      return Boolean.TRUE.equals( b );
   }

   @Function( "intAdd" )
   public static int intAdd( int x, int y ) {
      return x + y;
   }

   @Function( "trim" )
   public static String trim( @DefaultParam String str ) {
      return str.trim();
   }
}
