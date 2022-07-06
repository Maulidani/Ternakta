<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Article;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class ArticleController
{
    public function addArticle(Request $request)
    {   
        $user_store_id = $request->user_store_id;
        $title = $request->title;
        $description = $request->description;
        $image = $request->image;

        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {

                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();

                $check = in_array($extension, $allowedfileExtension);

                if ($check) {

                    $image->move(public_path() . '/image/article/', $filename);

                    $add_article = new Article;
                    $add_article->user_store_id = $user_store_id;
                    $add_article->title = $title;
                    $add_article->description = $description;
                    $add_article->image = $filename;
                    $add_article->save();
                   
                    if ($add_article) {

                        return response()->json([
                            'message' => 'Success',
                            'errors' => false,
                            'article' => $add_article
                        ]);

                    } else {

                        return response()->json([
                            'message' => 'Failed',
                            'errors' => true,
                        ]);
                    }

                } else {
                    return response()->json([
                        'message' => 'Failed',
                        'errors' => true,
                    ]);
                }

            } else {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            }
    }

    public function editArticle(Request $request)
    {
        $article_id = $request->article_id;
        $user_store_id = $request->user_store_id;
        $title = $request->title;
        $description = $request->description;
        $image = $request->image;

        $exist = Article::where(
            [
                'id'=> $article_id,
                'user_store_id'=> $user_store_id,
            ]
        )->exists();

        if ($exist){

            $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {
    
                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();
    
                $check = in_array($extension, $allowedfileExtension);
    
                if ($check) {
            
                    $image->move(public_path() . '/image/article/', $filename);
    
                    $edit_article = Article::find($article_id);
                    $edit_article->title = $request->title;
                    $edit_article->description = $description;   
                    $edit_article->image = $filename;
                    $edit_article->save();
    
                    if ($edit_article) {
    
                        return response()->json([
                            'message' => 'Success',
                            'errors' => false,
                            'article' => $edit_article
                        ]);
    
                    } else {
    
                        return response()->json([
                            'message' => 'Failed',
                            'errors' => true,
                        ]);
                    }

                } 

            } else {

                $edit_article = Article::find($article_id);
                $edit_article->title = $request->title;
                $edit_article->description = $description;   
                $edit_article->save();

                if ($edit_article) {

                    return response()->json([
                        'message' => 'Success',
                        'errors' => false,
                        'article' => $edit_article
                    ]);

                } else {

                    return response()->json([
                        'message' => 'Failed',
                        'errors' => true,
                    ]);
                }   
            }

        } else {
            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }
    }

    public function deleteArticle(Request $request)
    {
        $article_id = $request->article_id;
        $user_store_id = $request->user_store_id;

        $delete = Article::where(
            [
                'id'=> $article_id,
                'user_store_id'=> $user_store_id,
            ]
        )->delete();

        if($delete) {

            return response()->json([
                'message' => 'Success',
                'errors' => false,
            ]);

        } else {

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }
    }

    public function showArticle(Request $request)
    {
        $user_store_id = $request->user_store_id;
        $search = $request->search;

        if($user_store_id == '') {
            
            $article = Article::orderBy('created_at', 'DESC')
            ->where('title', 'like', "%" . $search . "%")
            ->get();
    
            if ($article->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $article,
                ]);
            }
        } else {

            $article = Article::orderBy('updated_at', 'DESC')
            ->where('user_store_id', $user_store_id)
            ->where('title', 'like', "%" . $search . "%")
            ->get();
    
            if ($article->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $article,
                ]);
            }
        }
    }

}